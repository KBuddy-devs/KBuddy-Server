package com.example.kbuddy_backend.qna.controller;


import com.example.kbuddy_backend.common.config.CurrentUser;
import com.example.kbuddy_backend.qna.dto.request.QnaCommentSaveRequest;
import com.example.kbuddy_backend.qna.dto.request.QnaSaveRequest;
import com.example.kbuddy_backend.qna.dto.response.QnaResponse;
import com.example.kbuddy_backend.qna.service.QnaCommentService;
import com.example.kbuddy_backend.qna.service.QnaService;
import com.example.kbuddy_backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/qna")
public class QnaController {

    private final QnaService qnaService;
    private final QnaCommentService qnaCommentService;

    //todo: 응답 dto 추가

    @PostMapping
    public ResponseEntity<?> saveQna(@RequestBody QnaSaveRequest qnaSaveRequest, @CurrentUser User user) {
        qnaService.saveQna(qnaSaveRequest, user);
        return ResponseEntity.ok().body("success");

    }

    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaResponse> getQna(@PathVariable Long qnaId) {
        QnaResponse qna = qnaService.getQna(qnaId);
        return ResponseEntity.ok().body(qna);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> saveQnaComment(@RequestBody QnaCommentSaveRequest qnaCommentSaveRequest,
                                            @CurrentUser User user) {
        qnaCommentService.saveQnaComment(qnaCommentSaveRequest, user);
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
}
