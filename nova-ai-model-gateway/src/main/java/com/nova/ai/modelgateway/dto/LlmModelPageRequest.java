package com.nova.ai.modelgateway.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LlmModelPageRequest {

    @NotNull
    @Min(1)
    private Integer pageNum;

    @NotNull
    @Min(1)
    @Max(200)
    private Integer pageSize;

    private Boolean enabled;
}
