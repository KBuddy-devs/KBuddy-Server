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

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qna_id")
	private Qna qna;

	private String imageUrl;
	private String originalFileName;
	private String filePath;
	private final LocalDateTime createdAt = LocalDateTime.now();

	@Builder
	public QnaImage(Qna qna, String imageUrl, String originalFileName, String filePath) {

		this.qna = qna;
		this.imageUrl = imageUrl;
		this.originalFileName = originalFileName;
		this.filePath = filePath;
	}

	public void setQna(Qna qna) {
		this.qna = qna;
	}
}
