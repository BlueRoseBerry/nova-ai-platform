package com.nova.ai.skill.model;

/**
 * JDK 25 Sealed Classes Showcase.
 * Skill type system is closed - only the permitted implementations are allowed.
 * This ensures compile-time exhaustive switch expressions and type safety.
 */
public sealed interface Skill
    permits DataQuerySkill, CalculationSkill, NotificationSkill, ExternalApiSkill {

    String id();
    String name();
    SkillType type();
    SkillResult execute(SkillContext context);
}

public enum SkillType {
    DATA_QUERY,
    CALCULATION,
    NOTIFICATION,
    EXTERNAL_API
}

public record SkillContext(
    String sessionId,
    String userId,
    java.util.Map<String, Object> params
) {}

public sealed interface SkillResult
    permits SuccessResult, ErrorResult {}

public record SuccessResult(String data) implements SkillResult {}
public record ErrorResult(String errorMessage) implements SkillResult {}
