package com.nova.ai.agent.model.agent;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AgentRegisterRequest {

    @NotBlank(message = "Agent 名称不能为空")
    private String name;

    private String role;

    @NotBlank(message = "系统提示词不能为空")
    private String systemPrompt;

    private List<String> skillIds;

    private Map<String, Object> config;

    private String modelId;

    private Double temperature;

    private Integer maxTokens;
}