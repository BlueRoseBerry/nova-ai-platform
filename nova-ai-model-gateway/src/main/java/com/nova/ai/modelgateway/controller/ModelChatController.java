package com.nova.ai.modelgateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.ai.common.response.BaseResponse;
import com.nova.ai.modelgateway.dto.OpenAiCompletionRequestPayload;
import com.nova.ai.modelgateway.dto.OpenAiCompletionResponsePayload;
import com.nova.ai.modelgateway.entity.LlmModelEntity;
import com.nova.ai.modelgateway.service.LlmModelService;
import com.nova.ai.modelgateway.service.OpenAiInferenceService;
import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/model/chat")
public class ModelChatController {

    private final LlmModelService llmModelService;
    private final OpenAiInferenceService openAiInferenceService;
    private final ObjectMapper objectMapper;

    public ModelChatController(
        LlmModelService llmModelService,
        OpenAiInferenceService openAiInferenceService,
        ObjectMapper objectMapper
    ) {
        this.llmModelService = llmModelService;
        this.openAiInferenceService = openAiInferenceService;
        this.objectMapper = objectMapper;
    }

    /** OpenAI 兼容 `/v1/chat/completions` JSON 的请求体语义；通过 `registryModelId` 选定本地注册的模型条目。 */
    @PostMapping("/completions")
    public BaseResponse<OpenAiCompletionResponsePayload> completions(
            @Valid @RequestBody OpenAiCompletionRequestPayload body) {
        LlmModelEntity model = llmModelService.requireEnabledForInvoke(body.getRegistryModelId());
        OpenAiCompletionResponsePayload result = openAiInferenceService.complete(model, body);
        return BaseResponse.success(result);
    }

    /** 与 {@link #completions} 同源请求体，通过 SSE（text/event-stream）实时转发上游 Chat Completions 流。 */
    @PostMapping(value = "/completions/stream", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<ServerSentEvent<String>>> completionsStream(
        @Valid @RequestBody OpenAiCompletionRequestPayload body
    ) {
        LlmModelEntity model = llmModelService.requireEnabledForInvoke(body.getRegistryModelId());
        Flux<ServerSentEvent<String>> flux =
            openAiInferenceService
                .streamComplete(model, body)
                .map(payload -> ServerSentEvent.<String>builder().data(payload).build())
                .onErrorResume(
                    exc ->
                        Flux.just(
                            ServerSentEvent.<String>builder().event("error").data(serializedError(exc)).build()
                        )
                );
        return ResponseEntity
            .ok()
            .cacheControl(CacheControl.noCache())
            .header("X-Accel-Buffering", "no")
            .header("Connection", "keep-alive")
            .body(flux);
    }

    private String serializedError(Throwable exc) {
        Map<String, String> m = new LinkedHashMap<>();
        String msg = exc.getMessage() != null ? exc.getMessage() : exc.getClass().getSimpleName();
        m.put("message", msg);
        try {
            return objectMapper.writeValueAsString(m);
        } catch (JsonProcessingException e) {
            return "{\"message\":\"" + msg.replace("\\", "\\\\").replace("\"", "\\\"") + "\"}";
        }
    }
}
