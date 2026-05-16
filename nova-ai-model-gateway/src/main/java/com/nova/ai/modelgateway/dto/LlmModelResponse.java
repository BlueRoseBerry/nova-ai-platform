package com.nova.ai.modelgateway.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class LlmModelResponse {

    private String id;
    private String name;
    private String provider;
    private String invokeFormat;
    private String remoteModel;
    private String baseUrl;
    private boolean hasApiSecret;
    private Double defaultTemperature;
    private Integer defaultMaxTokens;
    private Boolean enabled;
    private String description;
    private Map<String, Object> extraConfig = new HashMap<>();
    private long createdAtEpochMillis;
    private long updatedAtEpochMillis;
}
