package com.example.kbuddy_backend.qna.controller;


import com.example.kbuddy_backend.common.config.CurrentUser;
import com.example.kbuddy_backend.qna.dto.QnaSaveRequest;
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

    @PostMapping("/save")
    public ResponseEntity<?> saveQna(@RequestBody QnaSaveRequest qnaSaveRequest,@CurrentUser User user) {
        qnaService.saveQna(qnaSaveRequest,user);
        return ResponseEntity.ok().body("success");
//        qnaService.saveQna(qnaSaveRequest);
    }
}
