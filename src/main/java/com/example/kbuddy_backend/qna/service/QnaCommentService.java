package com.example.kbuddy_backend.qna.service;

import com.example.kbuddy_backend.qna.dto.request.QnaCommentSaveRequest;
import com.example.kbuddy_backend.qna.entity.Qna;
import com.example.kbuddy_backend.qna.entity.QnaComment;
import com.example.kbuddy_backend.qna.entity.QnaHeart;
import com.example.kbuddy_backend.qna.exception.DuplicatedQnaCommentHeartException;
import com.example.kbuddy_backend.qna.exception.QnaCommentNotFoundException;
import com.example.kbuddy_backend.qna.repository.QnaCommentRepository;
import com.example.kbuddy_backend.qna.repository.QnaHeartRepository;
import com.example.kbuddy_backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnaCommentService {


    private final QnaCommentRepository qnaCommentRepository;
    private final QnaService qnaService;

    @Transactional
    public void saveQnaComment(Long qnaId,QnaCommentSaveRequest qnaCommentSaveRequest, User user) {
        final Qna qna = qnaService.findQnaById(qnaId);
        QnaComment qnaComment = QnaComment.builder()
                .content(qnaCommentSaveRequest.content())
                .qna(qna)
                .writer(user)
                .build();
        qnaCommentRepository.save(qnaComment);
    }

    private QnaComment findQnaCommentById(Long commentId) {
        return qnaCommentRepository.findById(commentId)
                .orElseThrow(QnaCommentNotFoundException::new);
    }
}
