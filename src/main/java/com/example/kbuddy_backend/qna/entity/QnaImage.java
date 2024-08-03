package com.example.kbuddy_backend.qna.entity;

import com.example.kbuddy_backend.common.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class QnaImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id",nullable = false)
    private Qna qna;
    private String imageUrl;
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public QnaImage (Qna qna, String imageUrl) {
        this.qna = qna;
        this.imageUrl = imageUrl;
    }

    public void setQna(Qna qna) {
        this.qna = qna;
    }
}
