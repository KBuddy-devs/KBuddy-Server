package com.example.kbuddy_backend.qna.service;

import com.example.kbuddy_backend.qna.dto.request.QnaSaveRequest;
import com.example.kbuddy_backend.qna.dto.response.QnaResponse;
import com.example.kbuddy_backend.qna.entity.Qna;
import com.example.kbuddy_backend.qna.entity.QnaHeart;
import com.example.kbuddy_backend.qna.exception.DuplicateQnaHeartException;
import com.example.kbuddy_backend.qna.exception.QnaNotFoundException;
import com.example.kbuddy_backend.qna.repository.QnaHeartRepository;
import com.example.kbuddy_backend.qna.repository.QnaRepository;
import com.example.kbuddy_backend.user.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final QnaHeartRepository qnaHeartRepository;

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
    public void plusHeart(Long qnaId, User user) {
        qnaHeartRepository.findByQnaIdAndUserId(qnaId, user.getId())
                .ifPresent(qnaHeart -> {
                    throw new DuplicateQnaHeartException();
                });
        Qna qna = findQnaById(qnaId);
        QnaHeart qnaHeart = new QnaHeart(user, qna);
        qna.plusHeart(qnaHeart);
        qnaHeartRepository.save(qnaHeart);
    }

    @Transactional
    public void minusHeart(Long qnaId, User user) {

        Qna qnaById = findQnaById(qnaId);
        QnaHeart byQnaIdAndUserId = qnaHeartRepository.findByQnaIdAndUserId(qnaId, user.getId()).orElseThrow(QnaNotFoundException::new);
        qnaById.minusHeart(byQnaIdAndUserId);
        qnaHeartRepository.deleteByQnaIdAndUserId(qnaId, user.getId());
    }


    public Qna findQnaById(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(QnaNotFoundException::new);
    }

    //todo: commentCount 수정
    public QnaResponse makeQnaResponseDto(Qna qna) {
        return QnaResponse.of(qna.getId(), qna.getTitle(), qna.getDescription(), qna.getWriter().getUsername(),
                qna.getHeartCount(), qna.getCommentCount(), qna.getViewCount());
    }

    @Transactional
    public QnaResponse getQna(Long qnaId) {
        Qna qnaById = findQnaById(qnaId);
        qnaById.plustViewCount();
        return makeQnaResponseDto(qnaById);
    }
}
