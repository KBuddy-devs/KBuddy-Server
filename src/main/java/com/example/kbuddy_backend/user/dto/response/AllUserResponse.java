package com.example.kbuddy_backend.user.dto.response;

import java.util.List;

public record AllUserResponse(long totalElement, long totalPage, int currentPage, List<UserResponse> results) {
    public static AllUserResponse of(long totalElement, long totalPage, int currentPage, List<UserResponse> results) {
        return new AllUserResponse(totalElement, totalPage, currentPage, results);
    }
}
