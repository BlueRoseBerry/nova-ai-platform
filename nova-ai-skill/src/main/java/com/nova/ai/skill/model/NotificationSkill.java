package com.nova.ai.skill.model;

public record NotificationSkill(
    String id,
    String name,
    String channel,
    String template
) implements Skill {
    @Override
    public SkillType type() { return SkillType.NOTIFICATION; }
    @Override
    public SkillResult execute(SkillContext context) {
        return new SuccessResult("Notification sent via " + channel);
    }
}
