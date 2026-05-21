package com.nova.ai.agent.orchestrator;

import com.nova.ai.agent.model.*;
import com.nova.ai.modelgateway.dto.ChatMessagePayload;
import com.nova.ai.modelgateway.dto.OpenAiCompletionRequestPayload;
import com.nova.ai.modelgateway.dto.OpenAiCompletionResponsePayload;
import com.nova.ai.modelgateway.entity.LlmModelEntity;
import com.nova.ai.modelgateway.service.LlmModelService;
import com.nova.ai.modelgateway.service.OpenAiInferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Agent 编排器：使用结构化并发并行执行 RAG 知识检索、对话历史加载、工具注册查找，
 * 然后将收集的上下文组装为 LLM Chat Completions 请求，通过模型网关完成推理。
 */
@Component
public class AgentOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(AgentOrchestrator.class);

    private final LlmModelService llmModelService;
    private final OpenAiInferenceService openAiInferenceService;

    public AgentOrchestrator(LlmModelService llmModelService, OpenAiInferenceService openAiInferenceService) {
        this.llmModelService = llmModelService;
        this.openAiInferenceService = openAiInferenceService;
    }

    /**
     * 同步聊天：Agent 绑定的模型进行 Chat Completions 推理。
     */
    public AgentChatResponse chat(Agent agent, AgentChatRequest request) {
        LlmModelEntity model = llmModelService.requireEnabledForInvoke(agent.getModelId());
        List<ChatMessagePayload> enrichedMessages = buildMessages(agent, request);
        OpenAiCompletionRequestPayload payload = buildPayload(agent, model, enrichedMessages, request);
        OpenAiCompletionResponsePayload result = openAiInferenceService.complete(model, payload);
        return toChatResponse(agent, model, result);
    }

    /**
     * 流式聊天：Agent 绑定的模型进行 SSE 流式 Chat Completions 推理。
     */
    public reactor.core.publisher.Flux<String> chatStream(Agent agent, AgentChatRequest request) {
        LlmModelEntity model = llmModelService.requireEnabledForInvoke(agent.getModelId());
        List<ChatMessagePayload> enrichedMessages = buildMessages(agent, request);
        OpenAiCompletionRequestPayload payload = buildPayload(agent, model, enrichedMessages, request);
        return openAiInferenceService.streamComplete(model, payload);
    }

    /**
     * 带结构化并发的编排推理：并行加载 RAG、历史、工具，再组装为 LLM 请求。
     */
    public AgentResponse orchestrate(AgentRequest request)
            throws InterruptedException, ExecutionException {

        log.info("Starting agent orchestration for request: {}", request.getQuery());

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<RagResult> ragTask = executor.submit(
                () -> searchKnowledge(request.getQuery(), request.getKnowledgeBaseId()));

            Future<List<ChatMessage>> memoryTask = executor.submit(
                () -> loadConversationHistory(request.getSessionId(), 10));

            Future<List<ToolDefinition>> toolsTask = executor.submit(
                () -> getAvailableTools(request.getAgentId()));

            RagResult ragResult = ragTask.get();
            List<ChatMessage> history = memoryTask.get();
            List<ToolDefinition> tools = toolsTask.get();

            log.info("All subtasks completed. RAG chunks: {}, History messages: {}, Tools: {}",
                ragResult.getChunks().size(), history.size(), tools.size());

            return executeLlmInference(request, ragResult, history, tools);
        }
    }

    private List<ChatMessagePayload> buildMessages(Agent agent, AgentChatRequest request) {
        List<ChatMessagePayload> messages = new ArrayList<>();
        if (agent.getSystemPrompt() != null && !agent.getSystemPrompt().isBlank()) {
            messages.add(new ChatMessagePayload("system", agent.getSystemPrompt()));
        }
        for (ChatMessage msg : request.getMessages()) {
            messages.add(new ChatMessagePayload(msg.getRole(), msg.getContent()));
        }
        return messages;
    }

    private OpenAiCompletionRequestPayload buildPayload(
            Agent agent, LlmModelEntity model, List<ChatMessagePayload> messages, AgentChatRequest request) {
        OpenAiCompletionRequestPayload payload = new OpenAiCompletionRequestPayload();
        payload.setRegistryModelId(model.getId());
        payload.setMessages(messages);
        payload.setTemperature(resolveTemperature(agent, model, request));
        payload.setMaxTokens(resolveMaxTokens(agent, model, request));
        return payload;
    }

    private Double resolveTemperature(Agent agent, LlmModelEntity model, AgentChatRequest request) {
        if (request.getTemperature() != null) return request.getTemperature();
        if (agent.getTemperature() != null) return agent.getTemperature();
        return model.getDefaultTemperature();
    }

    private Integer resolveMaxTokens(Agent agent, LlmModelEntity model, AgentChatRequest request) {
        if (request.getMaxTokens() != null) return request.getMaxTokens();
        if (agent.getMaxTokens() != null) return agent.getMaxTokens();
        return model.getDefaultMaxTokens();
    }

    private AgentChatResponse toChatResponse(Agent agent, LlmModelEntity model,
                                              OpenAiCompletionResponsePayload result) {
        return new AgentChatResponse(
            agent.getId(),
            model.getId(),
            result.getRemoteModelUsed(),
            result.getContent(),
            result.getPromptTokens(),
            result.getCompletionTokens()
        );
    }

    private RagResult searchKnowledge(String query, String knowledgeBaseId) {
        log.info("Searching knowledge base: {}", knowledgeBaseId);
        try { Thread.sleep(300); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while searching knowledge base", e);
        }
        return new RagResult(List.of("Retrieved chunk 1", "Retrieved chunk 2"));
    }

    private List<ChatMessage> loadConversationHistory(String sessionId, int limit) {
        log.info("Loading conversation history for session: {}, limit: {}", sessionId, limit);
        try { Thread.sleep(100); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while loading conversation history", e);
        }
        return List.of(
            new ChatMessage("user", "Previous message 1"),
            new ChatMessage("assistant", "Previous response 1")
        );
    }

    private List<ToolDefinition> getAvailableTools(String agentId) {
        log.info("Loading tools for agent: {}", agentId);
        try { Thread.sleep(50); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while loading tools", e);
        }
        return List.of(
            new ToolDefinition("search", "Web search tool"),
            new ToolDefinition("calculator", "Math calculation tool")
        );
    }

    private AgentResponse executeLlmInference(AgentRequest request, RagResult rag,
                                               List<ChatMessage> history, List<ToolDefinition> tools) {
        log.info("Executing LLM inference with enriched context");
        try { Thread.sleep(500); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while executing LLM inference", e);
        }
        return new TextResponse("resp-001", "AI response based on RAG knowledge and conversation history.");
    }

    public static final class RagResult {

        private final List<String> chunks;

        public RagResult(List<String> chunks) {
            this.chunks = chunks;
        }

        public List<String> getChunks() {
            return chunks;
        }
    }

    public static final class ToolDefinition {

        private String name;
        private String description;

        public ToolDefinition() {
        }

        public ToolDefinition(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}