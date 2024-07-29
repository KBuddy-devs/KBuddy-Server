package com.example.kbuddy_backend.common.advice.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private HttpStatus status;
    private String path;
    private T message;
    private CustomErrorCode code;

    @Builder
    public ApiResponse(HttpStatus status, String path, T message, CustomErrorCode code) {
        this.status = status;
        this.path = path;
        this.message = message;
        this.code = code;
    }

}
