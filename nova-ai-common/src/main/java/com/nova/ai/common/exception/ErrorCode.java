package com.nova.ai.common.exception;

public enum ErrorCode {
    INVALID_REQUEST(400, "Invalid request parameters"),
    UNAUTHORIZED(401, "Unauthorized access"),
    FORBIDDEN(403, "Access denied"),
    NOT_FOUND(404, "Resource not found"),
    AGENT_NOT_FOUND(404, "Agent not found"),
    WORKFLOW_NOT_FOUND(404, "Workflow not found"),
    SKILL_NOT_FOUND(404, "Skill not found"),
    DIGITAL_HUMAN_NOT_FOUND(404, "Digital human not found"),
    KNOWLEDGE_BASE_NOT_FOUND(404, "Knowledge base not found"),
    MODEL_UNAVAILABLE(503, "Model service unavailable"),
    RATE_LIMIT_EXCEEDED(429, "Rate limit exceeded"),
    INTERNAL_ERROR(500, "Internal server error"),
    RAG_RETRIEVAL_FAILED(500, "RAG retrieval failed"),
    WORKFLOW_EXECUTION_FAILED(500, "Workflow execution failed"),
    SKILL_EXECUTION_FAILED(500, "Skill execution failed"),
    FFM_CALL_FAILED(500, "Native model inference failed"),
    OPENCLAW_PROTOCOL_ERROR(500, "OpenCLAW protocol error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() { return code; }
    public String message() { return message; }
}
