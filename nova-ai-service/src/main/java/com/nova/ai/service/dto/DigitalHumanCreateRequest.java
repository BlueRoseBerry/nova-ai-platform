package com.nova.ai.service.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record DigitalHumanCreateRequest(
    @NotBlank String name,
    String avatarUrl,
    String voiceModel,
    String personality,
    List<String> skills,
    String agentId,
    String workflowId
) {}
