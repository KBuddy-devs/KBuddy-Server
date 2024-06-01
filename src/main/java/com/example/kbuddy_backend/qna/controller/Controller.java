package com.example.kbuddy_backend.qna.controller;

import com.example.kbuddy_backend.qna.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final QnaService qnaService;

}
