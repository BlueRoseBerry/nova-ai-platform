package com.nova.ai.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record DigitalHumanEvent(
    @JsonProperty("event_id") String eventId,
    @JsonProperty("timestamp") Instant timestamp,
    @JsonProperty("type") EventType type,
    @JsonProperty("digital_human_id") String digitalHumanId,
    @JsonProperty("payload") String payload
) implements DomainEvent {

    public DigitalHumanEvent(String eventId, EventType type, String digitalHumanId, String payload) {
        this(eventId, Instant.now(), type, digitalHumanId, payload);
    }
}
