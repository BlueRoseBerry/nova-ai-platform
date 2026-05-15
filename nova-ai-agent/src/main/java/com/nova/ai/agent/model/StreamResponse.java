package com.nova.ai.agent.model;

public final class StreamResponse implements AgentResponse {

    private String responseId;
    private String streamId;

    public StreamResponse() {
    }

    public StreamResponse(String responseId, String streamId) {
        this.responseId = responseId;
        this.streamId = streamId;
    }

    @Override
    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }
}
