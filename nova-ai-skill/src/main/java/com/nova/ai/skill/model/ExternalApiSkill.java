package com.nova.ai.skill.model;

import java.util.Map;

public non-sealed class ExternalApiSkill implements Skill {
    private final String id;
    private final String name;
    private final String endpoint;
    private final Map<String, String> headers;

    public ExternalApiSkill(String id, String name, String endpoint, Map<String, String> headers) {
        this.id = id;
        this.name = name;
        this.endpoint = endpoint;
        this.headers = headers;
    }

    @Override
    public String id() { return id; }

    @Override
    public String name() { return name; }

    @Override
    public SkillType type() { return SkillType.EXTERNAL_API; }

    @Override
    public SkillResult execute(SkillContext context) {
        return new SuccessResult("API call to: " + endpoint);
    }
}
