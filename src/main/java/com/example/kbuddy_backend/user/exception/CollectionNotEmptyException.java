package com.example.kbuddy_backend.user.exception;

import com.example.kbuddy_backend.common.exception.BadRequestException;

public class CollectionNotEmptyException extends BadRequestException {
    public CollectionNotEmptyException() {
        super("컬렉션이 비어있지 않습니다.");
    }
}
