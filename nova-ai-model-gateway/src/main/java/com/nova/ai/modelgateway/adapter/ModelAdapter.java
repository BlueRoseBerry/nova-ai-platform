package com.nova.ai.modelgateway.adapter;

import java.util.Map;

public interface ModelAdapter {
    String getProvider();
    ChatResponse chat(ChatRequest request);
}

public record ChatRequest(
    String model,
    String prompt,
    double temperature,
    int maxTokens,
    Map<String, Object> extraParams
) {}

public record ChatResponse(
    String content,
    int promptTokens,
    int completionTokens,
    String model
) {}
