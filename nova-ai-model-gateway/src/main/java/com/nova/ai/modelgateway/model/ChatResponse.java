package com.nova.ai.modelgateway.model;

public record ChatResponse(
        String content,
        int promptTokens,
        int completionTokens,
        String model
) {}
