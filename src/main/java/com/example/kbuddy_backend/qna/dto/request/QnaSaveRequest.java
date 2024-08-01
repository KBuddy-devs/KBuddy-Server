package com.example.kbuddy_backend.qna.dto.request;

import java.util.List;

public record QnaSaveRequest(String title, String description, List<String> hashtags, Long categoryId) {
    public static QnaSaveRequest of(String title, String description, List<String> hashtags, Long categoryId) {
        return new QnaSaveRequest(title, description, hashtags, categoryId);
    }
}
