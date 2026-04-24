package com.nova.ai.common.exception;

public class NovaAiException extends RuntimeException {

    private final int code;

    public NovaAiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public NovaAiException(ErrorCode errorCode) {
        super(errorCode.message());
        this.code = errorCode.code();
    }

    public int getCode() {
        return code;
    }
}
