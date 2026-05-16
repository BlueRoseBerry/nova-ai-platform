package com.nova.ai.modelgateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    private String model;
    private String prompt;
    private double temperature;
    private int maxTokens;
    private Map<String, Object> extraParams;
}
