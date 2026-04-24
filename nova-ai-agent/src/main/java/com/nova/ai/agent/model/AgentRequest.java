package com.nova.ai.agent.model;

import java.util.List;

public record AgentRequest(
    String agentId,
    String sessionId,
    String query,
    String knowledgeBaseId,
    List<String> toolNames
) {}
