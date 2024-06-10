package com.example.kbuddy_backend.qna.controller;


import com.example.kbuddy_backend.common.config.CurrentUser;
import com.example.kbuddy_backend.qna.dto.request.QnaCommentSaveRequest;
import com.example.kbuddy_backend.qna.dto.request.QnaSaveRequest;
import com.example.kbuddy_backend.qna.service.QnaService;
import com.example.kbuddy_backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/qna")
public class QnaController {

    private final QnaService qnaService;

    @PostMapping
    public ResponseEntity<?> saveQna(@RequestBody QnaSaveRequest qnaSaveRequest,@CurrentUser User user) {
        qnaService.saveQna(qnaSaveRequest,user);
        return ResponseEntity.ok().body("success");
//        qnaService.saveQna(qnaSaveRequest);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> saveQnaComment(@RequestBody QnaCommentSaveRequest qnaCommentSaveRequest, @CurrentUser User user) {
        qnaService.saveQnaComment(qnaCommentSaveRequest, user);
        return ResponseEntity.ok().body("success");
    }
}
