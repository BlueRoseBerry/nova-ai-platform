package com.nova.ai.skill.model;

import java.util.Map;

public record DataQuerySkill(
    String id,
    String name,
    String sqlTemplate,
    Map<String, Object> dataSource
) implements Skill {

    @Override
    public SkillType type() { return SkillType.DATA_QUERY; }

    @Override
    public SkillResult execute(SkillContext context) {
        return new SuccessResult("Query result from: " + sqlTemplate);
    }
}
