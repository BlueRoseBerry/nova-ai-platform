package com.nova.ai.skill.impl;

import com.nova.ai.skill.model.*;
import java.util.Map;

public record CalculationSkill(
    String id,
    String name,
    String expression,
    Map<String, Object> parameters
) implements Skill {

    @Override
    public SkillType type() { return SkillType.CALCULATION; }

    @Override
    public SkillResult execute(SkillContext context) {
        return new SuccessResult("Calculation result: " + expression);
    }
}
