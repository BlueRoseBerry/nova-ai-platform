package com.nova.ai.agent.model;

import java.util.List;
import java.util.Map;

public record Agent(
    String id,
    String name,
    String role,
    String systemPrompt,
    List<String> skillIds,
    Map<String, Object> config,
    String modelId,
    double temperature,
    int maxTokens
) {}
