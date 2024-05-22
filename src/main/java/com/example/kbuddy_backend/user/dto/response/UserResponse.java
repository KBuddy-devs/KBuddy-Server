package com.example.kbuddy_backend.user.dto.response;

public record UserResponse(Long id, String username) {

    public static UserResponse of(Long id, String username) {

        return new UserResponse(id, username);
    }
}
