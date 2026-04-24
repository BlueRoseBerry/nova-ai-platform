package com.nova.ai.service.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record DigitalHumanUpdateRequest(
    @NotNull Long id,
    String name,
    String avatarUrl,
    String voiceModel,
    String personality,
    List<String> skills,
    String agentId,
    String workflowId
) {}
