package com.nova.ai.modelgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 对齐内部 {@link com.nova.ai.modelgateway.model.ChatResponse}，增加注册表条目 id 便于链路追踪。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiCompletionResponsePayload {

    private String registryModelId;

    private String content;

    private int promptTokens;

    private int completionTokens;

    /** 实际发往上游的模型名（remote_model） */
    private String remoteModelUsed;
}
