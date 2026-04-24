package com.nova.ai.service.dto;

import java.time.Instant;
import java.util.List;

public record DigitalHumanResponse(
    Long id,
    String userId,
    String name,
    String avatarUrl,
    String voiceModel,
    String personality,
    List<String> skills,
    String agentId,
    String workflowId,
    String publishStatus,
    Instant createdAt,
    Instant updatedAt
) {}
