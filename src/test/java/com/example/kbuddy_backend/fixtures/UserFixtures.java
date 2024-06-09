package com.example.kbuddy_backend.fixtures;

import com.example.kbuddy_backend.user.entity.User;

public class UserFixtures {
    public static final String EMAIL = "k-buddy@gmail.com";
    public static final String USERNAME = "k-buddy";
    public static final String PASSWORD = "1q2w3e";

    public static User createUser() {
        return User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build();
    }
}
