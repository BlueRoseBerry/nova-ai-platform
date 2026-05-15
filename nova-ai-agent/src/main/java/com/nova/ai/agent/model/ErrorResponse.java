package com.nova.ai.agent.model;

public final class ErrorResponse implements AgentResponse {

    private String responseId;
    private int code;
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String responseId, int code, String message) {
        this.responseId = responseId;
        this.code = code;
        this.message = message;
    }

    @Override
    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
