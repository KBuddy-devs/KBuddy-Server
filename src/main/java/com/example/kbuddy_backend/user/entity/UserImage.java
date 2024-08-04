package com.example.kbuddy_backend.user.entity;

import java.time.LocalDateTime;

import com.example.kbuddy_backend.qna.entity.Qna;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
	private String originalFileName;
	private String filePath;
	private final LocalDateTime createdAt = LocalDateTime.now();

	@Builder
	public UserImage(User user, String imageUrl, String originalFileName, String filePath) {
		this.user = user;
		this.imageUrl = imageUrl;
		this.originalFileName = originalFileName;
		this.filePath = filePath;
	}

	public void update(String imageUrl, String originalFileName, String filePath) {
		this.imageUrl = imageUrl;
		this.originalFileName = originalFileName;
		this.filePath = filePath;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
