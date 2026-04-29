package com.nova.ai.agent.model;

public record ToolCallResponse(
    String responseId,
    java.util.List<ToolCall> toolCalls
) implements AgentResponse {}
