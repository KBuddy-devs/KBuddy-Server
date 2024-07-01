package com.example.kbuddy_backend.common.advice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String message;
    private int code;

    public ErrorResponse(final String message) {
        this.message = message;
    }

    public ErrorResponse(final String message, final int code) {
        this.message = message;
        this.code = code;
    }
}
