package com.nova.ai.agent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentChatResponse {

    private String agentId;
    private String modelId;
    private String remoteModelUsed;
    private String content;
    private int promptTokens;
    private int completionTokens;
}