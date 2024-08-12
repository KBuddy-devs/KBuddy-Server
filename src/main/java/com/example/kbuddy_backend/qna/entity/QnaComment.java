package com.example.kbuddy_backend.qna.entity;

import com.example.kbuddy_backend.common.entity.BaseTimeEntity;
import com.example.kbuddy_backend.qna.dto.response.QnaCommentResponse;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private Qna qna;

    public void setQna(Qna qna) {
        this.qna = qna;
    }

    @Builder
    public QnaComment(User writer, Qna qna, String content) {
        this.content = content;
        this.writer = writer;
        this.qna = qna;
    }


    public void addQna(Qna qna) {
        this.qna = qna;
        qna.addComment(this);
    }



}
