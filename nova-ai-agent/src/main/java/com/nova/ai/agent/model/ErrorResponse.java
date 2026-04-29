package com.nova.ai.agent.model;

public record ErrorResponse(
    String responseId,
    int code,
    String message
) implements AgentResponse {}
