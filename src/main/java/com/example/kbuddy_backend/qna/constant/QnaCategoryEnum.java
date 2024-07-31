package com.example.kbuddy_backend.qna.constant;

import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum QnaCategoryEnum {

    VISA("비자",1),
    HOSPITAL("병원",2),
    FOOD("음식",3),
    SHOPPING("쇼핑",4),
    TRAVEL("여행",5),
    ETC("기타",6);

    private final String category;
    private final int categoryId;

    public static Optional<QnaCategoryEnum> findCategoryById(int categoryId){
        for (QnaCategoryEnum category : QnaCategoryEnum.values()) {
            if (category.getCategoryId() == categoryId) {
                return Optional.of(category);
            }
        }
        return Optional.empty();
    }
}
