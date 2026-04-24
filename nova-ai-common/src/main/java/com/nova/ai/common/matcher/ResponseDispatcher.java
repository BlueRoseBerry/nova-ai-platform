package com.nova.ai.common.matcher;

import com.nova.ai.common.event.AgentEvent;
import com.nova.ai.common.event.DigitalHumanEvent;
import com.nova.ai.common.event.DomainEvent;
import com.nova.ai.common.event.SkillEvent;
import com.nova.ai.common.event.WorkflowEvent;

/**
 * JDK 25 Pattern Matching Showcase: switch expression with sealed interface,
 * record patterns, and guarded patterns for domain event dispatching.
 */
public class ResponseDispatcher {

    public String dispatch(DomainEvent event) {
        return switch (event) {
            case AgentEvent ae when ae.agentId() != null && !ae.agentId().isEmpty() ->
                handleAgentEvent(ae);
            case AgentEvent ae ->
                "Unhandled agent event: " + ae.eventId();
            case WorkflowEvent we when we.instanceId() != null ->
                handleWorkflowEvent(we);
            case WorkflowEvent we ->
                "Unhandled workflow event: " + we.eventId();
            case DigitalHumanEvent dh ->
                handleDigitalHumanEvent(dh);
            case SkillEvent se when se.skillId() != null ->
                handleSkillEvent(se);
            case null, default ->
                throw new IllegalArgumentException("Unknown event type");
        };
    }

    private String handleAgentEvent(AgentEvent event) {
        return "[AGENT] %s | agent=%s | type=%s".formatted(
            event.eventId(), event.agentId(), event.type());
    }

    private String handleWorkflowEvent(WorkflowEvent event) {
        return "[WORKFLOW] %s | workflow=%s | instance=%s".formatted(
            event.eventId(), event.workflowId(), event.instanceId());
    }

    private String handleDigitalHumanEvent(DigitalHumanEvent event) {
        return "[DIGITAL_HUMAN] %s | id=%s | type=%s".formatted(
            event.eventId(), event.digitalHumanId(), event.type());
    }

    private String handleSkillEvent(SkillEvent event) {
        return "[SKILL] %s | skill=%s | type=%s".formatted(
            event.eventId(), event.skillId(), event.type());
    }
}
