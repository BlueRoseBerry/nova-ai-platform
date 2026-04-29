package com.nova.ai.agent.model;

public record StreamResponse(
    String responseId,
    String streamId
) implements AgentResponse {}
