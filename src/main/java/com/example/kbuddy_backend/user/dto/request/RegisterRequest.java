package com.example.kbuddy_backend.user.dto.request;

//todo: validation
public record RegisterRequest(String userId, String email, String password, String firstName, String lastName) {
    public static RegisterRequest of(String userId, String email, String password, String firstName, String lastName) {
        return new RegisterRequest(userId, email, password, firstName, lastName);
    }
}