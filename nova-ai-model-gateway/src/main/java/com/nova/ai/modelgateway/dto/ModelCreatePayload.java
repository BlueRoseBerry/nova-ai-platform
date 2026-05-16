package com.nova.ai.modelgateway.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class ModelCreatePayload {

    @NotBlank(message = "id 不能为空")
    @Size(max = 64)
    private String id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 32)
    private String provider;

    @NotBlank
    @Size(max = 64)
    private String invokeFormat;

    @NotBlank
    @Size(max = 255)
    private String remoteModel;

    @Size(max = 512)
    private String baseUrl;

    private String apiKey;

    private Double defaultTemperature;

    @Min(1)
    @Max(1_000_000)
    private Integer defaultMaxTokens;

    @NotNull
    private Boolean enabled;

    private String description;

    private Map<String, Object> extraConfig;
}
