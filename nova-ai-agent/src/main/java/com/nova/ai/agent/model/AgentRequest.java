package com.nova.ai.agent.model;

import java.util.List;

public class AgentRequest {

    private String agentId;
    private String sessionId;
    private String query;
    private String knowledgeBaseId;
    private List<String> toolNames;

    public AgentRequest() {
    }

    public AgentRequest(
        String agentId,
        String sessionId,
        String query,
        String knowledgeBaseId,
        List<String> toolNames
    ) {
        this.agentId = agentId;
        this.sessionId = sessionId;
        this.query = query;
        this.knowledgeBaseId = knowledgeBaseId;
        this.toolNames = toolNames;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public void setKnowledgeBaseId(String knowledgeBaseId) {
        this.knowledgeBaseId = knowledgeBaseId;
    }

    public List<String> getToolNames() {
        return toolNames;
    }

    public void setToolNames(List<String> toolNames) {
        this.toolNames = toolNames;
    }
}
