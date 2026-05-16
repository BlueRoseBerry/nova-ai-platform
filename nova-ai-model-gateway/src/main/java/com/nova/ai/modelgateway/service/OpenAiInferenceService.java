package com.nova.ai.modelgateway.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nova.ai.common.exception.ErrorCode;
import com.nova.ai.common.exception.NovaAiException;
import com.nova.ai.modelgateway.config.NovaModelProperties;
import com.nova.ai.modelgateway.dto.ChatMessagePayload;
import com.nova.ai.modelgateway.dto.OpenAiCompletionRequestPayload;
import com.nova.ai.modelgateway.dto.OpenAiCompletionResponsePayload;
import com.nova.ai.modelgateway.entity.LlmModelEntity;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OpenAiInferenceService {

    private static final String FORMAT_OPENAI = "openai_chat_completions";
    private static final Duration REQUEST_TIMEOUT = Duration.ofMinutes(2);

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final NovaModelProperties novaModelProperties;

    public OpenAiInferenceService(
            WebClient webClient,
            ObjectMapper objectMapper,
            NovaModelProperties novaModelProperties) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.novaModelProperties = novaModelProperties;
    }

    public OpenAiCompletionResponsePayload complete(LlmModelEntity model, OpenAiCompletionRequestPayload request) {
        if (!FORMAT_OPENAI.equals(model.getInvokeFormat())) {
            throw new NovaAiException(
                ErrorCode.INVALID_REQUEST.code(),
                "仅支持 invoke_format=%s ，当前条目为: %s".formatted(FORMAT_OPENAI, model.getInvokeFormat())
            );
        }
        String endpoint = normalizeBase(resolveBaseUrl(model)) + "/chat/completions";
        String apiKey = resolveApiKey(model);
        if (!StringUtils.hasText(apiKey)) {
            throw new NovaAiException(ErrorCode.MODEL_UNAVAILABLE.code(), "缺少 API Key：请在模型注册条目或 nova.model.providers 中配置");
        }

        ObjectNode body = buildOpenAiPayload(model, request);

        try {
            JsonNode root = webClient.post()
                .uri(endpoint)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey.trim())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .timeout(REQUEST_TIMEOUT)
                .block();
            return mapResponse(root, model.getId(), model.getRemoteModel());
        } catch (WebClientResponseException e) {
            String upstream = shorten(e.getResponseBodyAsString(), 4096);
            throw new NovaAiException(
                ErrorCode.MODEL_UNAVAILABLE.code(),
                "上游 HTTP %s — %s".formatted(e.getStatusCode().value(), upstream)
            );
        } catch (Exception e) {
            throw new NovaAiException(ErrorCode.MODEL_UNAVAILABLE.code(), "调用模型异常: " + e.getMessage());
        }
    }

    /**
     * 流式 Chat Completions：解析上游 SSE 每条 {@code data:}（含 JSON chunk 或 {@code [DONE]}），原样透传给网关 SSE。
     */
    public Flux<String> streamComplete(LlmModelEntity model, OpenAiCompletionRequestPayload request) {
        if (!FORMAT_OPENAI.equals(model.getInvokeFormat())) {
            return Flux.error(
                new NovaAiException(
                    ErrorCode.INVALID_REQUEST.code(),
                    "仅支持 invoke_format=%s ，当前条目为: %s".formatted(FORMAT_OPENAI, model.getInvokeFormat())
                )
            );
        }
        String endpoint = normalizeBase(resolveBaseUrl(model)) + "/chat/completions";
        String apiKey = resolveApiKey(model);
        if (!StringUtils.hasText(apiKey)) {
            return Flux.error(
                new NovaAiException(ErrorCode.MODEL_UNAVAILABLE.code(), "缺少 API Key：请在模型注册条目或 nova.model.providers 中配置")
            );
        }

        ObjectNode body = buildOpenAiPayload(model, request);
        body.put("stream", true);

        return webClient
            .post()
            .uri(endpoint)
            .header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey.trim())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(body)
            .exchangeToFlux(response -> {
                if (response.statusCode().isError()) {
                    return response
                        .bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .flatMapMany(
                            err ->
                                Flux.error(
                                    new NovaAiException(
                                        ErrorCode.MODEL_UNAVAILABLE.code(),
                                        "上游 HTTP %s — %s"
                                            .formatted(response.statusCode().value(), shorten(err, 4096))
                                    )
                                )
                        );
                }
                MediaType contentType = response.headers().contentType().orElse(null);
                if (contentType != null && MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                    return response
                        .bodyToMono(String.class)
                        .timeout(REQUEST_TIMEOUT)
                        .flatMapMany(this::jsonCompletionBodyToStreamPayloads);
                }
                return parseUpstreamSseBody(response.bodyToFlux(DataBuffer.class)).timeout(REQUEST_TIMEOUT);
            });
    }

    /**
     * 按字节块累积并按行切分 SSE；{@code bodyToFlux(String)} 会在行中间切断，导致 {@code data:} 无法识别。
     */
    private Flux<String> parseUpstreamSseBody(Flux<DataBuffer> body) {
        SseLineAccumulator acc = new SseLineAccumulator();
        return body
            .concatMap(
                db -> {
                    byte[] bytes = new byte[db.readableByteCount()];
                    db.read(bytes);
                    DataBufferUtils.release(db);
                    String chunk = new String(bytes, StandardCharsets.UTF_8);
                    List<String> payloads = acc.ingest(chunk);
                    return Flux.fromIterable(payloads);
                }
            )
            .concatWith(
                Flux.defer(
                    () -> {
                        List<String> tail = acc.finish();
                        return tail.isEmpty() ? Flux.empty() : Flux.fromIterable(tail);
                    }
                )
            );
    }

    /** 上游未返回 event-stream、而是一次性 JSON 时，合成一条 OpenAI 风格 delta chunk 供前端解析。 */
    private Flux<String> jsonCompletionBodyToStreamPayloads(String body) {
        if (!StringUtils.hasText(body)) {
            return Flux.error(new NovaAiException(ErrorCode.MODEL_UNAVAILABLE.code(), "上游返回空响应体"));
        }
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode err = root.path("error");
            if (!err.isMissingNode() && !err.isNull()) {
                return Flux.error(
                    new NovaAiException(ErrorCode.MODEL_UNAVAILABLE.code(), "上游错误: " + shorten(err.toString(), 4096))
                );
            }
            JsonNode choices = root.path("choices");
            if (!choices.isArray() || choices.isEmpty()) {
                return Flux.error(
                    new NovaAiException(ErrorCode.MODEL_UNAVAILABLE.code(), "上游 JSON 缺少 choices: " + shorten(body, 512))
                );
            }
            JsonNode first = choices.get(0);
            String content = first.path("message").path("content").asText("");
            if (!StringUtils.hasText(content)) {
                content = first.path("message").path("reasoning_content").asText("");
            }
            if (!StringUtils.hasText(content)) {
                content = first.path("text").asText("");
            }
            if (!StringUtils.hasText(content)) {
                return Flux.error(
                    new NovaAiException(ErrorCode.MODEL_UNAVAILABLE.code(), "上游 JSON 无可用正文: " + shorten(body, 512))
                );
            }
            ObjectNode chunk = objectMapper.createObjectNode();
            ArrayNode chArr = objectMapper.createArrayNode();
            ObjectNode choice = objectMapper.createObjectNode();
            choice.put("index", 0);
            ObjectNode delta = objectMapper.createObjectNode();
            delta.put("content", content.trim());
            choice.set("delta", delta);
            chArr.add(choice);
            chunk.set("choices", chArr);
            JsonNode usage = root.path("usage");
            if (!usage.isMissingNode() && !usage.isNull()) {
                chunk.set("usage", usage);
            }
            return Flux.just(objectMapper.writeValueAsString(chunk));
        } catch (NovaAiException e) {
            return Flux.error(e);
        } catch (Exception e) {
            return Flux.error(
                new NovaAiException(ErrorCode.MODEL_UNAVAILABLE.code(), "无法解析上游 JSON: " + e.getMessage())
            );
        }
    }

    private static final class SseLineAccumulator {
        private final StringBuilder buffer = new StringBuilder();

        List<String> ingest(String chunk) {
            buffer.append(chunk);
            return drainCompleteLines(false);
        }

        List<String> finish() {
            return drainCompleteLines(true);
        }

        private List<String> drainCompleteLines(boolean flushTail) {
            List<String> out = new ArrayList<>();
            while (true) {
                int nl = indexOfLineFeed(buffer);
                if (nl < 0) {
                    if (flushTail && !buffer.isEmpty()) {
                        String line = buffer.toString().replace("\r", "").trim();
                        buffer.setLength(0);
                        extractSseDataPayload(line).ifPresent(out::add);
                    }
                    break;
                }
                String line = buffer.substring(0, nl).replace("\r", "").trim();
                buffer.delete(0, nl + 1);
                if (!line.isEmpty()) {
                    extractSseDataPayload(line).ifPresent(out::add);
                }
            }
            return out;
        }

        private static int indexOfLineFeed(StringBuilder sb) {
            for (int i = 0; i < sb.length(); i++) {
                if (sb.charAt(i) == '\n') {
                    return i;
                }
            }
            return -1;
        }
    }

    private static Optional<String> extractSseDataPayload(String line) {
        if (line == null || line.isEmpty()) {
            return Optional.empty();
        }
        if (line.startsWith(":")) {
            return Optional.empty();
        }
        if (line.startsWith("event:") || line.startsWith("id:") || line.startsWith("retry:")) {
            return Optional.empty();
        }
        String payload;
        if (line.startsWith("data:")) {
            payload = line.substring(5).trim();
        } else if (line.startsWith("{") || line.startsWith("[")) {
            payload = line;
        } else {
            return Optional.empty();
        }
        return payload.isEmpty() ? Optional.empty() : Optional.of(payload);
    }

    private OpenAiCompletionResponsePayload mapResponse(JsonNode root, String registryId, String remoteModelUsed) {
        if (root == null || root.isMissingNode() || root.isNull()) {
            throw new NovaAiException(ErrorCode.MODEL_UNAVAILABLE.code(), "上游返回空响应体");
        }
        JsonNode choices = root.path("choices");
        if (!choices.isArray() || choices.isEmpty()) {
            throw new NovaAiException(ErrorCode.MODEL_UNAVAILABLE.code(), "上游响应缺少 choices: " + root);
        }
        JsonNode msg = choices.get(0).path("message");
        String content = msg.path("content").asText("");
        if (!StringUtils.hasText(content)) {
            content = msg.path("reasoning_content").asText("");
        }
        JsonNode usage = root.path("usage");
        int promptTok = usage.path("prompt_tokens").asInt(0);
        int completionTok = usage.path("completion_tokens").asInt(0);
        return new OpenAiCompletionResponsePayload(registryId, content.trim(), promptTok, completionTok, remoteModelUsed);
    }

    private ObjectNode buildOpenAiPayload(LlmModelEntity model, OpenAiCompletionRequestPayload request) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("model", model.getRemoteModel());
        Double temp = request.getTemperature() != null ? request.getTemperature() : model.getDefaultTemperature();
        if (temp != null) {
            root.put("temperature", temp);
        }
        Integer maxTok = request.getMaxTokens() != null ? request.getMaxTokens() : model.getDefaultMaxTokens();
        if (maxTok != null) {
            root.put("max_tokens", maxTok);
        }
        ArrayNode arr = objectMapper.createArrayNode();
        appendMessages(arr, request.getMessages());
        root.set("messages", arr);
        return root;
    }

    private void appendMessages(ArrayNode arr, List<ChatMessagePayload> messages) {
        for (ChatMessagePayload m : messages) {
            ObjectNode o = objectMapper.createObjectNode();
            o.put("role", m.getRole() == null ? "" : m.getRole().trim());
            o.put("content", m.getContent() != null ? m.getContent() : "");
            arr.add(o);
        }
    }

    private String resolveBaseUrl(LlmModelEntity entity) {
        if (StringUtils.hasText(entity.getBaseUrl())) {
            return entity.getBaseUrl().trim();
        }
        NovaModelProperties.ProviderBinding fb = fallbackProvider(entity.getProvider());
        if (fb != null && StringUtils.hasText(fb.getBaseUrl())) {
            return fb.getBaseUrl().trim();
        }
        NovaModelProperties.ProviderBinding openai = fallbackProvider("openai");
        if (openai != null && StringUtils.hasText(openai.getBaseUrl())) {
            return openai.getBaseUrl().trim();
        }
        throw new NovaAiException(ErrorCode.MODEL_UNAVAILABLE.code(), "缺少 base-url：请在注册条目或 nova.model.providers 中配置");
    }

    private String resolveApiKey(LlmModelEntity entity) {
        if (StringUtils.hasText(entity.getApiKey())) {
            return entity.getApiKey().trim();
        }
        NovaModelProperties.ProviderBinding fb = fallbackProvider(entity.getProvider());
        if (fb != null && StringUtils.hasText(fb.getApiKey())) {
            return fb.getApiKey().trim();
        }
        NovaModelProperties.ProviderBinding openai = fallbackProvider("openai");
        if (openai != null && StringUtils.hasText(openai.getApiKey())) {
            return openai.getApiKey().trim();
        }
        String env = System.getenv("OPENAI_API_KEY");
        return env == null ? null : env.trim();
    }

    private NovaModelProperties.ProviderBinding fallbackProvider(String providerKey) {
        if (!StringUtils.hasText(providerKey) || novaModelProperties.getProviders() == null) {
            return null;
        }
        return novaModelProperties.getProviders().get(providerKey.trim().toLowerCase());
    }

    /**
     * 去除尾部 slash，假定下游为 POST {base}/chat/completions。
     */
    private static String normalizeBase(String raw) {
        String s = raw.trim();
        if (s.endsWith("/")) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }

    private static String shorten(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() <= max ? s : s.substring(0, max) + "...";
    }
}
