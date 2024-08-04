package com.example.kbuddy_backend.qna.dto.response;

import java.time.LocalDateTime;

public record QnaCommentResponse(Long id, Long qnaId, Long writerId, String description,
                                 int heartCount, LocalDateTime createdAt, LocalDateTime modifiedAt,
                                 boolean remove) {
    public static QnaCommentResponse of(Long id, Long qnaId, Long writerId, String description,
                                        int heartCount,LocalDateTime createdAt, LocalDateTime modifiedAt,
                                        boolean remove) {
        return new QnaCommentResponse(id, qnaId, writerId, description, heartCount, createdAt,modifiedAt, remove);
    }
}