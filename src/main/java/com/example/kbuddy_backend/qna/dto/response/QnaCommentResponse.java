package com.example.kbuddy_backend.qna.dto.response;

public record QnaCommentResponse(Long id, Long qnaId, Long writerId, String description,
                                 int heartCount,
                                 boolean remove) {
    public static QnaCommentResponse of(Long id, Long qnaId, Long writerId, String description,
                                        int heartCount,
                                        boolean remove) {
        return new QnaCommentResponse(id, qnaId, writerId, description, heartCount, remove);
    }
}
