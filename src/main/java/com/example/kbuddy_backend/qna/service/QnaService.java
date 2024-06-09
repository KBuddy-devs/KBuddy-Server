package com.example.kbuddy_backend.qna.service;

import com.example.kbuddy_backend.qna.dto.QnaSaveRequest;
import com.example.kbuddy_backend.qna.entity.Qna;
import com.example.kbuddy_backend.qna.repository.QnaRepository;
import com.example.kbuddy_backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;


    public void saveQna(QnaSaveRequest qnaSaveRequest,User user) {
        Qna qna = Qna.builder()
                .title(qnaSaveRequest.title())
                .description(qnaSaveRequest.description())
                .writer(user)
                .build();

        qnaRepository.save(qna);
    }
}
