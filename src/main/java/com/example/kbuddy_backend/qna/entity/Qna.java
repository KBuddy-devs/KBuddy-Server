package com.example.kbuddy_backend.qna.entity;

import com.example.kbuddy_backend.common.entity.BaseTimeEntity;
import com.example.kbuddy_backend.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToMany(mappedBy = "qna")
    private List<QnaCategory> category = new ArrayList<>();

    private String title;
    private String description;

    private Long viewCount;

    @Builder
    public Qna(User writer, String title, String description) {
        this.writer = writer;
        this.title = title;
        this.description = description;

    }

}
