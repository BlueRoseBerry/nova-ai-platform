package com.nova.ai.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseResponse<T>(
    int code,
    String message,
    T data,
    long timestamp
) implements Serializable {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, "success", data, System.currentTimeMillis());
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(200, message, data, System.currentTimeMillis());
    }

    public static <T> BaseResponse<T> fail(int code, String message) {
        return new BaseResponse<>(code, message, null, System.currentTimeMillis());
    }

    public static <T> BaseResponse<T> fail(String message) {
        return new BaseResponse<>(500, message, null, System.currentTimeMillis());
    }
}
