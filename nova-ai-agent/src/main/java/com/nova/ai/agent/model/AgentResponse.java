package com.nova.ai.agent.model;

public sealed interface AgentResponse
    permits TextResponse, ToolCallResponse, StreamResponse, ErrorResponse {
    String getResponseId();
}
