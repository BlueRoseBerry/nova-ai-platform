package com.nova.ai.workflow.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record WorkflowDefinition(
    String workflowId,
    String name,
    String description,
    List<NodeDefinition> nodes,
    Map<String, Object> metadata,
    Instant createdAt,
    Instant updatedAt
) {}
