package com.nova.ai.modelgateway.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatMessagePayload {

    @NotBlank(message = "role 不能为空")
    private String role;

    @NotBlank(message = "content 不能为空")
    private String content;
}
