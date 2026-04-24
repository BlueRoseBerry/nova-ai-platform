package com.nova.ai.service.dto;

import java.util.List;

public record DigitalHumanPageResponse(
    long total,
    long pages,
    long current,
    long size,
    List<DigitalHumanResponse> records
) {}
