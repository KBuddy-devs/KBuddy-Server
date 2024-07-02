package com.example.kbuddy_backend.user.dto.response;

import java.time.LocalDateTime;

public record UserResponse(Long id, String userName, String email, String profileImageUrl, String bio, LocalDateTime createdDate,String firstName,String lastName) {

    public static UserResponse of(Long id, String userName,String email, String profileImageUrl, String bio, LocalDateTime createdDate,String firstName,String lastName) {
        return new UserResponse(id, userName, email, profileImageUrl, bio, createdDate,firstName,lastName);
    }
}
