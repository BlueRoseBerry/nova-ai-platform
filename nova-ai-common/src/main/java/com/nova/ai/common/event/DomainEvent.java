package com.nova.ai.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.Instant;

public sealed interface DomainEvent extends Serializable
    permits AgentEvent, WorkflowEvent, DigitalHumanEvent, SkillEvent {

    String eventId();
    Instant timestamp();
    EventType type();
}
