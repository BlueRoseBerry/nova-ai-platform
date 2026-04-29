package com.nova.ai.agent.model;

public record TextResponse(
    String responseId,
    String content
) implements AgentResponse {}
