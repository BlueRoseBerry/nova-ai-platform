package com.nova.ai.modelgateway.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Nova 网关对 OpenAI Chat Completions 的调用请求：{@code registryModelId} 为本地注册表 llm_model.id；
 * messages 结构与 OpenAI 一致。
 */
@Data
public class OpenAiCompletionRequestPayload {

    @NotBlank
    private String registryModelId;

    @NotEmpty
    private List<@Valid ChatMessagePayload> messages;

    private Double temperature;

    private Integer maxTokens;
}
