package com.nova.ai.agent.model;

public sealed interface AgentResponse
    permits TextResponse, ToolCallResponse, StreamResponse, ErrorResponse {
    String responseId();
}

public record TextResponse(
    String responseId,
    String content
) implements AgentResponse {}

public record ToolCallResponse(
    String responseId,
    java.util.List<ToolCall> toolCalls
) implements AgentResponse {}

public record StreamResponse(
    String responseId,
    String streamId
) implements AgentResponse {}

public record ErrorResponse(
    String responseId,
    int code,
    String message
) implements AgentResponse {}

record ToolCall(
    String toolId,
    String toolName,
    String parameters
) {}
