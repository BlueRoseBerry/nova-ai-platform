package com.nova.ai.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record AgentEvent(
    @JsonProperty("event_id") String eventId,
    @JsonProperty("timestamp") Instant timestamp,
    @JsonProperty("type") EventType type,
    @JsonProperty("agent_id") String agentId,
    @JsonProperty("session_id") String sessionId,
    @JsonProperty("payload") String payload
) implements DomainEvent {

    public AgentEvent(String eventId, EventType type, String agentId, String payload) {
        this(eventId, Instant.now(), type, agentId, null, payload);
    }
}
