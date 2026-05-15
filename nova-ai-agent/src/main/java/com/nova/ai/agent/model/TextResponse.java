package com.nova.ai.agent.model;

public final class TextResponse implements AgentResponse {

    private String responseId;
    private String content;

    public TextResponse() {
    }

    public TextResponse(String responseId, String content) {
        this.responseId = responseId;
        this.content = content;
    }

    @Override
    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
