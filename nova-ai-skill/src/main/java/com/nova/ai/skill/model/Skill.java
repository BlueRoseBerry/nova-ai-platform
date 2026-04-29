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

