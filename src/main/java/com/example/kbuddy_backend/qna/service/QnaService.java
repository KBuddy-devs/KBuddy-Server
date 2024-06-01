package com.example.kbuddy_backend.qna.service;

import com.example.kbuddy_backend.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
}
