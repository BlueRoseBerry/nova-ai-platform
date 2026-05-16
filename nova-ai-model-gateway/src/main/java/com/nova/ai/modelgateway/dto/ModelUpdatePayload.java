package com.nova.ai.modelgateway.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class ModelUpdatePayload {

    @NotBlank
    @Size(max = 64)
    private String id;

    @Size(max = 255)
    private String name;

    @Size(max = 32)
    private String provider;

    @Size(max = 64)
    private String invokeFormat;

    @Size(max = 255)
    private String remoteModel;

    @Size(max = 512)
    private String baseUrl;

    /** {@code null} 表示不改变已存密钥；非 null 则表示覆盖为该值（空字符串等同于清空并使用配置/env 回落）。 */
    private String apiKeySecretUpdate;

    private Double defaultTemperature;

    @Min(1)
    @Max(1_000_000)
    private Integer defaultMaxTokens;

    private Boolean enabled;

    private String description;

    private Map<String, Object> extraConfig;
}
