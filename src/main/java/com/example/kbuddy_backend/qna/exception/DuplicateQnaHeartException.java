package com.example.kbuddy_backend.qna.exception;

public class DuplicateQnaHeartException extends RuntimeException {
    public DuplicateQnaHeartException() {
        super("이미 좋아요를 누른 질문 게시글입니다.");
    }
}
