package com.nova.ai.agent.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AgentChatRequest {

    @NotBlank(message = "agentId 不能为空")
    private String agentId;

    @NotEmpty(message = "messages 不能为空")
    private List<@jakarta.validation.Valid ChatMessage> messages;

    private Double temperature;

    private Integer maxTokens;
}