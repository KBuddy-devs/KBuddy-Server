package com.example.kbuddy_backend.user.dto.request;

//todo: validation
public record LoginRequest(String username, String email, String password) {
}