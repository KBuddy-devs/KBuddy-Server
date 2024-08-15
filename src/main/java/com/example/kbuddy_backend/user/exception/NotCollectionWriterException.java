package com.example.kbuddy_backend.user.exception;

import com.example.kbuddy_backend.common.exception.BadRequestException;

public class NotCollectionWriterException extends BadRequestException {
    public NotCollectionWriterException() {
        super("컬렉션 주인이 아닙니다.");
    }
}
