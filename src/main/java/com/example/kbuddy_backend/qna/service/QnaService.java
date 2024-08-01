package com.example.kbuddy_backend.qna.service;

import com.example.kbuddy_backend.qna.dto.request.QnaSaveRequest;
import com.example.kbuddy_backend.qna.dto.response.QnaResponse;
import com.example.kbuddy_backend.qna.entity.Qna;
import com.example.kbuddy_backend.qna.entity.QnaCategory;
import com.example.kbuddy_backend.qna.entity.QnaHeart;
import com.example.kbuddy_backend.qna.entity.QnaImage;
import com.example.kbuddy_backend.qna.exception.DuplicatedQnaHeartException;
import com.example.kbuddy_backend.qna.exception.QnaNotFoundException;
import com.example.kbuddy_backend.qna.repository.QnaCategoryRepository;
import com.example.kbuddy_backend.qna.repository.QnaHeartRepository;
import com.example.kbuddy_backend.qna.repository.QnaRepository;
import com.example.kbuddy_backend.s3.dto.response.S3Response;
import com.example.kbuddy_backend.s3.exception.ImageUploadException;
import com.example.kbuddy_backend.s3.service.S3Service;
import com.example.kbuddy_backend.user.entity.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final QnaHeartRepository qnaHeartRepository;
    private final QnaCategoryRepository qnaCategoryRepository;
    private final S3Service s3Service;
    private static final String FOLDER_NAME = "qna";

    @Transactional
    public void saveQna(QnaSaveRequest qnaSaveRequest, List<MultipartFile> imageFiles, User user) {

        String hashtag = String.join(",", qnaSaveRequest.hashtags());
        Optional<QnaCategory> qnaCategory = qnaCategoryRepository.findById(qnaSaveRequest.categoryId());
        if (qnaCategory.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");
        }
        Qna qna = Qna.builder()
                .title(qnaSaveRequest.title())
                .description(qnaSaveRequest.description())
                .category(qnaCategory.get())
                .hashtag(hashtag)
                .writer(user)
                .build();

        //todo: 트랜잭션 실패 시 이미지 삭제하는 로직 추가
        if (imageFiles != null && !imageFiles.isEmpty()) {
            saveImageFiles(imageFiles, qna);
        }
        qnaRepository.save(qna);
    }

    private void saveImageFiles(List<MultipartFile> imageFiles, Qna qna) {
        for (MultipartFile imageFile : imageFiles) {
            if (!s3Service.checkImageFile(imageFile)) {
                throw new ImageUploadException("이미지 파일이 아닙니다.");
            }
            S3Response s3Response = s3Service.saveFileWithUUID(imageFile, FOLDER_NAME);
            QnaImage qnaImage = QnaImage.builder()
                    .qna(qna)
                    .imageUrl(s3Response.s3ImageUrl())
                    .build();
            qna.addImage(qnaImage);
        }
    }


    @Transactional
    public void plusHeart(Long qnaId, User user) {
        qnaHeartRepository.findByQnaIdAndUserId(qnaId, user.getId())
                .ifPresent(qnaHeart -> {
                    throw new DuplicatedQnaHeartException();
                });
        Qna qna = findQnaById(qnaId);
        QnaHeart qnaHeart = new QnaHeart(user, qna);
        qna.plusHeart(qnaHeart);
        qnaHeartRepository.save(qnaHeart);
    }

    @Transactional
    public void minusHeart(Long qnaId, User user) {

        Qna qnaById = findQnaById(qnaId);
        QnaHeart byQnaIdAndUserId = qnaHeartRepository.findByQnaIdAndUserId(qnaId, user.getId())
                .orElseThrow(QnaNotFoundException::new);
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
        qnaById.plusViewCount();
        return makeQnaResponseDto(qnaById);
    }
}
