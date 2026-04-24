package com.nova.ai.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record WorkflowEvent(
    @JsonProperty("event_id") String eventId,
    @JsonProperty("timestamp") Instant timestamp,
    @JsonProperty("type") EventType type,
    @JsonProperty("workflow_id") String workflowId,
    @JsonProperty("instance_id") String instanceId,
    @JsonProperty("step_id") String stepId,
    @JsonProperty("payload") String payload
) implements DomainEvent {

    public WorkflowEvent(String eventId, EventType type, String workflowId, String instanceId, String payload) {
        this(eventId, Instant.now(), type, workflowId, instanceId, null, payload);
    }
}
