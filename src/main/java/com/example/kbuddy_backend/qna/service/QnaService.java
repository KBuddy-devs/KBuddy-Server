package com.example.kbuddy_backend.qna.service;

import com.example.kbuddy_backend.qna.dto.request.QnaCommentSaveRequest;
import com.example.kbuddy_backend.qna.dto.request.QnaSaveRequest;
import com.example.kbuddy_backend.qna.entity.Qna;
import com.example.kbuddy_backend.qna.entity.QnaComment;
import com.example.kbuddy_backend.qna.exception.QnaNotFoundException;
import com.example.kbuddy_backend.qna.repository.QnaCommentRepository;
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
    private final QnaCommentRepository qnaCommentRepository;


    @Transactional
    public void saveQna(QnaSaveRequest qnaSaveRequest, User user) {

        Qna qna = Qna.builder()
                .title(qnaSaveRequest.title())
                .description(qnaSaveRequest.description())
                .writer(user)
                .build();

        qnaRepository.save(qna);
    }

    @Transactional
    public void saveQnaComment(QnaCommentSaveRequest qnaCommentSaveRequest, User user) {
        final Qna qna = qnaRepository.findById(qnaCommentSaveRequest.qnaId()).orElseThrow(QnaNotFoundException::new);
        QnaComment qnaComment = QnaComment.builder()
                .content(qnaCommentSaveRequest.content())
                .qna(qna)
                .writer(user)
                .build();
        qnaCommentRepository.save(qnaComment);
    }
}
