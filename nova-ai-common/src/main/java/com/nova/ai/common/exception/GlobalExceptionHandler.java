package com.nova.ai.common.exception;

import com.nova.ai.common.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<Void> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .collect(Collectors.joining(", "));
        log.warn("Validation error: {}", message);
        return BaseResponse.fail(ErrorCode.INVALID_REQUEST.code(), message);
    }

    @ExceptionHandler(NovaAiException.class)
    public BaseResponse<Void> handleNovaAiException(NovaAiException e) {
        log.warn("Business error: code={}, message={}", e.getCode(), e.getMessage());
        return BaseResponse.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<Void> handleGenericException(Exception e) {
        log.error("Unexpected error", e);
        return BaseResponse.fail(500, "Internal server error: " + e.getMessage());
    }
}
