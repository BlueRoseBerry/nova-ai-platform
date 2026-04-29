package com.nova.ai.modelgateway.model;

import java.util.Map;

public record ChatRequest(
        String model,
        String prompt,
        double temperature,
        int maxTokens,
        Map<String, Object> extraParams
) {}