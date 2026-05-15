package com.nova.ai.agent.model;

public class ToolCall {

    private String toolId;
    private String toolName;
    private String parameters;

    public ToolCall() {
    }

    public ToolCall(String toolId, String toolName, String parameters) {
        this.toolId = toolId;
        this.toolName = toolName;
        this.parameters = parameters;
    }

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
