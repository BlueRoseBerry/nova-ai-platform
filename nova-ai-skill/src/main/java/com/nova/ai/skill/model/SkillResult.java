package com.nova.ai.skill.model;

public sealed interface SkillResult
    permits SuccessResult, ErrorResult {}
