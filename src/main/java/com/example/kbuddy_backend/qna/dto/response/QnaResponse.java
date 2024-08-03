package com.example.kbuddy_backend.qna.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record QnaResponse(Long id, Long writerId, Long categoryId, String title, String description, int viewCount, LocalDateTime createdAt, LocalDateTime modifiedAt, boolean remove,
                          List<String> images, List<QnaCommentResponse> comments, int heartCount, int commentCount) {
    public static QnaResponse of(Long id, Long writerId, Long categoryId, String title, String description, int viewCount, LocalDateTime createdAt, LocalDateTime modifiedAt, boolean remove,
                                 List<String> images, List<QnaCommentResponse> comments, int heartCount, int commentCount) {
        return new QnaResponse(id, writerId, categoryId, title, description, viewCount, createdAt, modifiedAt, remove, images, comments, heartCount, commentCount);
    }
}
