package com.nova.ai.agent.model;

import java.util.List;

public final class ToolCallResponse implements AgentResponse {

    private String responseId;
    private List<ToolCall> toolCalls;

    public ToolCallResponse() {
    }

    public ToolCallResponse(String responseId, List<ToolCall> toolCalls) {
        this.responseId = responseId;
        this.toolCalls = toolCalls;
    }

    @Override
    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public List<ToolCall> getToolCalls() {
        return toolCalls;
    }

    public void setToolCalls(List<ToolCall> toolCalls) {
        this.toolCalls = toolCalls;
    }
}
