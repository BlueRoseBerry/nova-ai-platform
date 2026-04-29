package com.nova.ai.workflow.model;

public enum NodeType {
    LLM_CALL,
    API_CALL,
    CONDITION,
    LOOP,
    HUMAN_REVIEW,
    SKILL_EXECUTION,
    PARALLEL
}
