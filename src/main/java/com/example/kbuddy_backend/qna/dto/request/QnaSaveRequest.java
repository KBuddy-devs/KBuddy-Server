package com.example.kbuddy_backend.qna.dto.request;

import com.example.kbuddy_backend.qna.constant.QnaCategoryEnum;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record QnaSaveRequest(String title, String description, List<String> hashtags, int categoryId) {
    public static QnaSaveRequest of(String title, String description, List<String> hashtags,int categoryId) {
        return new QnaSaveRequest(title, description, hashtags,categoryId);
    }
}
