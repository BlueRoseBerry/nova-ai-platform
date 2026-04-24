package com.nova.ai.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record DigitalHumanPageRequest(
    @Min(1) int pageNum,
    @Min(1) @Max(100) int pageSize
) {
    public DigitalHumanPageRequest {
        if (pageNum < 1) pageNum = 1;
        if (pageSize < 1) pageSize = 10;
    }

    public DigitalHumanPageRequest() {
        this(1, 10);
    }
}
