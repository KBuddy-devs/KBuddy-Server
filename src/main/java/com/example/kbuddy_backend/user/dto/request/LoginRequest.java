package com.example.kbuddy_backend.user.dto.request;

//todo: validation
public record LoginRequest(String username, String email, String password) {
    public static LoginRequest of(String username, String email, String password) {
        return new LoginRequest(username, email, password);
    }
}