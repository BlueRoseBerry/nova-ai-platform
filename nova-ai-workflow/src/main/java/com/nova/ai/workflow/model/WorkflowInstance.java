package com.nova.ai.workflow.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record WorkflowInstance(
    String instanceId,
    String workflowId,
    WorkflowStatus status,
    String currentNodeId,
    Map<String, Object> context,
    Instant startedAt,
    Instant completedAt
) {}

