package com.example.kbuddy_backend.qna.dto.response;

import java.time.LocalDateTime;

public record QnaPaginationResponse(Long id, Long writerId, Long categoryId, String title, String description,
                                    int viewCount, int heartCount,
                                    int commentCount, LocalDateTime createdAt, LocalDateTime modifiedAt,
                                    boolean remove) {
    public static QnaPaginationResponse of(Long id, Long writerId, Long categoryId, String title, String description,
                                           int viewCount, int heartCount,
                                           int commentCount, LocalDateTime createdAt, LocalDateTime modifiedAt,
                                           boolean remove) {
        return new QnaPaginationResponse(id, writerId, categoryId, title, description, viewCount, heartCount,
                commentCount, createdAt, modifiedAt, remove);
    }
}