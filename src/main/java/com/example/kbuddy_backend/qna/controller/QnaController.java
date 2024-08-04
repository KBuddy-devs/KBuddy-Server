package com.example.kbuddy_backend.qna.controller;

import com.example.kbuddy_backend.common.config.CurrentUser;
import com.example.kbuddy_backend.qna.dto.request.QnaCommentSaveRequest;
import com.example.kbuddy_backend.qna.dto.request.QnaImageRequest;
import com.example.kbuddy_backend.qna.dto.request.QnaSaveRequest;
import com.example.kbuddy_backend.qna.dto.request.QnaUpdateRequest;
import com.example.kbuddy_backend.qna.dto.response.AllQnaResponse;
import com.example.kbuddy_backend.qna.dto.response.QnaResponse;
import com.example.kbuddy_backend.qna.service.QnaCommentService;
import com.example.kbuddy_backend.qna.service.QnaService;
import com.example.kbuddy_backend.user.entity.User;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("kbuddy/v1/qna")
public class QnaController {

	private final QnaService qnaService;
	private final QnaCommentService qnaCommentService;

	//todo: 응답 dto 추가
	@PostMapping
	public ResponseEntity<QnaResponse> saveQna(@RequestPart(required = false) List<MultipartFile> images,
		@RequestPart(value = "request") QnaSaveRequest qnaSaveRequest, @CurrentUser User user) {
		QnaResponse qnaResponse = qnaService.saveQna(qnaSaveRequest, images, user);
		return ResponseEntity.ok().body(qnaResponse);
	}

	//전체 조회 (페이징)
	@GetMapping
	public ResponseEntity<AllQnaResponse> getAllQna(@PageableDefault(size = 5,sort = "createdDate",direction = Sort.Direction.DESC) Pageable pageable) {
		AllQnaResponse allQnaResponse = qnaService.getAllQna(pageable);
		return ResponseEntity.ok().body(allQnaResponse);
	}

	@GetMapping("/{qnaId}")
	public ResponseEntity<QnaResponse> getQna(@PathVariable Long qnaId) {
		QnaResponse qna = qnaService.getQna(qnaId);
		return ResponseEntity.ok().body(qna);
	}

	@PatchMapping("/{qnaId}")
	public ResponseEntity<QnaResponse> updateQna(@PathVariable Long qnaId,
		@RequestBody QnaUpdateRequest qnaUpdateRequest, @CurrentUser User user) {
		QnaResponse qna = qnaService.updateQna(qnaId, qnaUpdateRequest, user);
		return ResponseEntity.ok().body(qna);
	}

	@PostMapping("/{qnaId}/images")
	public ResponseEntity<String> addQnaImages(@PathVariable Long qnaId, @RequestPart List<MultipartFile> images,
		@CurrentUser User user) {
		qnaService.addImages(qnaId, images, user);
		return ResponseEntity.ok().body("이미지가 성공적으로 추가되었습니다.");
	}

	@DeleteMapping("/{qnaId}/images")
	public ResponseEntity<String> deleteQnaImages(@PathVariable Long qnaId, @RequestBody QnaImageRequest images,
		@CurrentUser User user) {
		qnaService.deleteImages(qnaId, images, user);
		return ResponseEntity.ok().body("이미지가 성공적으로 삭제되었습니다.");
	}

	@DeleteMapping("/{qnaId}")
	public ResponseEntity<String> deleteQna(@PathVariable Long qnaId, @CurrentUser User user) {
		qnaService.deleteQna(qnaId, user);
		return ResponseEntity.ok().body("QnA가 성공적으로 삭제되었습니다.");
	}

	@PostMapping("/{qnaId}/comments")
	public ResponseEntity<?> saveQnaComment(@PathVariable Long qnaId,
		@RequestBody QnaCommentSaveRequest qnaCommentSaveRequest,
		@CurrentUser User user) {
		qnaCommentService.saveQnaComment(qnaId, qnaCommentSaveRequest, user);
		return ResponseEntity.ok().body("success");
	}

	//Qna 좋아요
	@PostMapping("/{qnaId}/hearts")
	public ResponseEntity<?> plusQnaHeart(@PathVariable Long qnaId, @CurrentUser User user) {
		qnaService.plusHeart(qnaId, user);
		return ResponseEntity.ok().body("success");
	}

	//Qna 좋아요 취소
	@DeleteMapping("/{qnaId}/hearts")
	public ResponseEntity<?> minusQnaHeart(@PathVariable Long qnaId, @CurrentUser User user) {
		qnaService.minusHeart(qnaId, user);
		return ResponseEntity.ok().body("success");
	}

	//댓글 좋아요
	@PostMapping("/comments/{commentId}/hearts")
	public ResponseEntity<?> plusCommentHeart(@PathVariable Long commentId, @CurrentUser User user) {
		qnaCommentService.plusHeart(commentId, user);
		return ResponseEntity.ok().body("success");
	}

	//댓글 좋아요 취소
	@DeleteMapping("/comments/{commentId}/hearts")
	public ResponseEntity<?> minusCommentHeart(@PathVariable Long commentId, @CurrentUser User user) {
		qnaCommentService.minusHeart(commentId, user);
		return ResponseEntity.ok().body("success");
	}
}
