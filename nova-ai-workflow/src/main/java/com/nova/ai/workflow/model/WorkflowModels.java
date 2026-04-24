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

public record NodeDefinition(
    String id,
    String name,
    NodeType type,
    Map<String, Object> config,
    List<String> nextNodeIds,
    Map<String, String> branches,
    long timeoutMs
) {}

public enum NodeType {
    LLM_CALL,
    API_CALL,
    CONDITION,
    LOOP,
    HUMAN_REVIEW,
    SKILL_EXECUTION,
    PARALLEL
}

public record WorkflowInstance(
    String instanceId,
    String workflowId,
    WorkflowStatus status,
    String currentNodeId,
    Map<String, Object> context,
    Instant startedAt,
    Instant completedAt
) {}

public enum WorkflowStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED,
    PAUSED,
    WAITING_HUMAN_REVIEW
}
