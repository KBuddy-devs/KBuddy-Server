package com.example.kbuddy_backend.common.advice.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private LocalDateTime timestamp = LocalDateTime.now();
    private String status;
    private String path;
    private T message;
    private CustomErrorCode code;


}
