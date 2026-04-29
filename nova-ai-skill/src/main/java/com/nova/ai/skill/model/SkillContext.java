package com.nova.ai.skill.model;

public record SkillContext(
    String sessionId,
    String userId,
    java.util.Map<String, Object> params
) {}
