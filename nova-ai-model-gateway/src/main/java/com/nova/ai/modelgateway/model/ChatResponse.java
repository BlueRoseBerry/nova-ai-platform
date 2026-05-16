package com.nova.ai.modelgateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String content;
    private int promptTokens;
    private int completionTokens;
    private String model;
}
