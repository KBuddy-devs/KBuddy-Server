package com.example.kbuddy_backend.user.entity;

import com.example.kbuddy_backend.common.constant.ImageFileType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private ImageFileType fileType;
    private String filePath;
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public UserImage(User user, String imageUrl, ImageFileType fileType, String filePath) {
        this.user = user;
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public void update(String imageUrl, ImageFileType fileType, String filePath) {
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.filePath = filePath;
    }
}
