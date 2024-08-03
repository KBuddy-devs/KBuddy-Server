package com.example.kbuddy_backend.qna.service;

import com.example.kbuddy_backend.qna.dto.request.QnaSaveRequest;
import com.example.kbuddy_backend.qna.dto.request.QnaUpdateRequest;
import com.example.kbuddy_backend.qna.dto.response.QnaCommentResponse;
import com.example.kbuddy_backend.qna.dto.response.QnaResponse;
import com.example.kbuddy_backend.qna.entity.Qna;
import com.example.kbuddy_backend.qna.entity.QnaCategory;
import com.example.kbuddy_backend.qna.entity.QnaHeart;
import com.example.kbuddy_backend.qna.entity.QnaImage;
import com.example.kbuddy_backend.qna.exception.DuplicatedQnaHeartException;
import com.example.kbuddy_backend.qna.exception.NotWriterException;
import com.example.kbuddy_backend.qna.exception.QnaNotFoundException;
import com.example.kbuddy_backend.qna.repository.QnaCategoryRepository;
import com.example.kbuddy_backend.qna.repository.QnaCommentRepository;
import com.example.kbuddy_backend.qna.repository.QnaHeartRepository;
import com.example.kbuddy_backend.qna.repository.QnaRepository;
import com.example.kbuddy_backend.s3.dto.response.S3Response;
import com.example.kbuddy_backend.s3.exception.ImageUploadException;
import com.example.kbuddy_backend.s3.service.S3Service;
import com.example.kbuddy_backend.user.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnaService {

	private final QnaRepository qnaRepository;
	private final QnaHeartRepository qnaHeartRepository;
	private final QnaCommentRepository qnaCommentRepository;
	private final QnaCategoryRepository qnaCategoryRepository;
	private final S3Service s3Service;
	private static final String FOLDER_NAME = "qna";

	@Transactional
	public QnaResponse saveQna(QnaSaveRequest qnaSaveRequest, List<MultipartFile> imageFiles, User user) {

		String hashtag = String.join(",", qnaSaveRequest.hashtags());
		QnaCategory qnaCategory = findCategoryById(qnaSaveRequest.categoryId());

		Qna qna = Qna.builder()
			.title(qnaSaveRequest.title())
			.description(qnaSaveRequest.description())
			.category(qnaCategory)
			.hashtag(hashtag)
			.writer(user)
			.build();

		//todo: 트랜잭션 실패 시 이미지 삭제하는 로직 추가
		if (imageFiles != null && !imageFiles.isEmpty()) {
			saveImageFiles(imageFiles, qna);
		}

		Qna saveQna = qnaRepository.save(qna);
		return createQnaResponseDto(saveQna);
	}

	@Transactional
	public QnaResponse getQna(Long qnaId) {
		Qna qnaById = findQnaById(qnaId);
		qnaById.plusViewCount();
		return createQnaResponseDto(qnaById);
	}

	@Transactional
	public QnaResponse updateQna(Long qnaId, QnaUpdateRequest qnaUpdateRequest, User user) {

		Qna qnaById = findQnaById(qnaId);

		isQnaWriter(user, qnaById);

		String hashtag = String.join(",", qnaUpdateRequest.hashtags());
		QnaCategory categoryById = findCategoryById(qnaUpdateRequest.categoryId());
		qnaById.update(qnaUpdateRequest.title(), qnaUpdateRequest.description(), hashtag, categoryById);
		return createQnaResponseDto(qnaById);
	}

	private static void isQnaWriter(User user, Qna qnaById) {
		if (!Objects.equals(qnaById.getWriter().getId(), user.getId())) {
			throw new NotWriterException();
		}
	}

	@Transactional
	public void deleteQna(Long qnaId, User user) {
		Qna qna = qnaRepository.findById(qnaId).orElseThrow(QnaNotFoundException::new);

		//해당 게시글 작성자가 아닐 경우
		isQnaWriter(user, qna);
		qna.delete();
	}

	private QnaResponse createQnaResponseDto(Qna qna) {
		List<String> images = qna.getImageUrls().stream().map(QnaImage::getImageUrl).toList();
		List<QnaCommentResponse> comments = qna.getComments()
			.stream()
			.map(qnaComment ->
				QnaCommentResponse.of(qnaComment.getId(), qnaComment.getQna().getId(), qnaComment.getWriter().getId(),
					qnaComment.getContent(), qnaComment.getHeartCount(), qnaComment.getCreatedDate(),
					qnaComment.getLastModifiedDate(), qnaComment.isDelYn()))
			.sorted(Comparator.comparing(QnaCommentResponse::createdAt))
			.toList();

		return QnaResponse.of(qna.getId(), qna.getWriter().getId(), qna.getCategory().getId(), qna.getTitle(),
			qna.getDescription(), qna.getViewCount(), qna.getCreatedDate(), qna.getLastModifiedDate(),
			qna.isDelYn(), images, comments, qna.getHeartCount(), qna.getCommentCount());
	}

	private void saveImageFiles(List<MultipartFile> imageFiles, Qna qna) {
		for (MultipartFile imageFile : imageFiles) {
			if (!s3Service.checkImageFile(imageFile)) {
				throw new ImageUploadException("이미지 파일이 아닙니다.");
			}
			S3Response s3Response = s3Service.saveFileWithUUID(imageFile, FOLDER_NAME);
			QnaImage qnaImage = QnaImage.builder()
				.qna(qna)
				.imageUrl(s3Response.s3ImageUrl())
				.build();
			qna.addImage(qnaImage);
		}
	}

	@Transactional
	public void plusHeart(Long qnaId, User user) {
		qnaHeartRepository.findByQnaIdAndUserId(qnaId, user.getId())
			.ifPresent(qnaHeart -> {
				throw new DuplicatedQnaHeartException();
			});
		Qna qna = findQnaById(qnaId);
		QnaHeart qnaHeart = new QnaHeart(user, qna);
		qna.plusHeart(qnaHeart);
		qnaHeartRepository.save(qnaHeart);
	}

	@Transactional
	public void minusHeart(Long qnaId, User user) {

		Qna qnaById = findQnaById(qnaId);
		QnaHeart byQnaIdAndUserId = qnaHeartRepository.findByQnaIdAndUserId(qnaId, user.getId())
			.orElseThrow(QnaNotFoundException::new);
		qnaById.minusHeart(byQnaIdAndUserId);
		qnaHeartRepository.deleteByQnaIdAndUserId(qnaId, user.getId());
	}

	public Qna findQnaById(Long qnaId) {
		return qnaRepository.findById(qnaId).orElseThrow(QnaNotFoundException::new);
	}

	private QnaCategory findCategoryById(Long categoryId) {
		return qnaCategoryRepository.findById(categoryId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
	}

	//todo: commentCount 수정

}
