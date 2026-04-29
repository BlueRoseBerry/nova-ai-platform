package com.nova.ai.workflow.model;

import java.util.List;
import java.util.Map;

public record NodeDefinition(
    String id,
    String name,
    NodeType type,
    Map<String, Object> config,
    List<String> nextNodeIds,
    Map<String, String> branches,
    long timeoutMs
) {}
