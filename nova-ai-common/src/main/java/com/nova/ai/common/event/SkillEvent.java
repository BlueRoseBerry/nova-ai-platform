package com.nova.ai.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record SkillEvent(
    @JsonProperty("event_id") String eventId,
    @JsonProperty("timestamp") Instant timestamp,
    @JsonProperty("type") EventType type,
    @JsonProperty("skill_id") String skillId,
    @JsonProperty("payload") String payload
) implements DomainEvent {

    public SkillEvent(String eventId, EventType type, String skillId, String payload) {
        this(eventId, Instant.now(), type, skillId, payload);
    }
}
