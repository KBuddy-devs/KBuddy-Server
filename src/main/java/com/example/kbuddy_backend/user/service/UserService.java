package com.example.kbuddy_backend.user.service;

import com.example.kbuddy_backend.common.dto.ImageFileDto;
import com.example.kbuddy_backend.qna.entity.QnaCollection;
import com.example.kbuddy_backend.qna.repository.QnaCollectionRepository;
import com.example.kbuddy_backend.qna.service.QnaService;
import com.example.kbuddy_backend.user.dto.request.CollectionRequest;
import com.example.kbuddy_backend.user.dto.request.UserProfileUpdateRequest;
import com.example.kbuddy_backend.user.dto.response.AllUserResponse;
import com.example.kbuddy_backend.user.dto.response.UserResponse;
import com.example.kbuddy_backend.user.entity.User;
import com.example.kbuddy_backend.user.entity.UserImage;
import com.example.kbuddy_backend.user.exception.CollectionNotEmptyException;
import com.example.kbuddy_backend.user.exception.NotCollectionWriterException;
import com.example.kbuddy_backend.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final QnaCollectionRepository qnaCollectionRepository;
    private final QnaService qnaService;


    public UserResponse getUser(User user) {
        User findUser = findUserById(user.getId());
        return createUserDto(findUser);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
    }

    private User findUserByUUID(String uuid) {
        UUID userUuid = UUID.fromString(uuid);
        return userRepository.findByUuid(userUuid).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
    }

    public AllUserResponse getAllUsers(Pageable pageable) {
        Page<User> allUsers = userRepository.findAll(pageable);
        return AllUserResponse.of(allUsers.getTotalElements(), allUsers.getTotalPages(), allUsers.getNumber(),
                allUsers.stream().map(user -> createUserDto(user)).toList()
        );
    }

    @Transactional
    public void updateProfile(UserProfileUpdateRequest request, String userId) {

        User userById = findUserByUUID(userId);
        userById.updateProfile(request.userId(), request.email(), request.country(), request.gender(), request.bio(),
                request.firstName(), request.lastName());
        if (request.profileImage() != null) {
            UserImage userImage = UserImage.builder()
                    .user(userById)
                    .imageUrl(request.profileImage().url())
                    .fileType(request.profileImage().type())
                    .filePath(request.profileImage().name())
                    .build();
            userById.setImageUrls(userImage);
        }
    }

    private UserResponse createUserDto(User findUser) {
        List<String> authorities = findUser.getAuthorities().stream()
                .map(authority -> authority.getAuthorityName().name())
                .toList();
        return UserResponse.of(findUser.getUuid().toString(), findUser.getUsername(), findUser.getEmail(), authorities,
                findUser.getImageUrls() == null ? null : ImageFileDto.of(findUser.getImageUrls().getFileType(),
                        findUser.getImageUrls().getFilePath(), findUser.getImageUrls().getImageUrl()),
                findUser.getBio(), findUser.getFirstName(), findUser.getLastName(),
                findUser.getCreatedDate(), findUser.getGender(), findUser.getCountry(), findUser.isActive());
    }

    @Transactional
    public void saveCollection(CollectionRequest collectionRequest, User user) {
        final String collectionTitle = collectionRequest.collectionTitle();
        QnaCollection qnaCollection = new QnaCollection(collectionTitle, user);
        qnaCollectionRepository.save(qnaCollection);
    }

    @Transactional
    public void updateCollection(String userId, Long collectionId, CollectionRequest collectionRequest) {
        final String collectionTitle = collectionRequest.collectionTitle();
        QnaCollection collectionById = qnaService.findCollectionById(collectionId);

        isCollectionWriter(userId, collectionById);

        collectionById.updateCollectionName(collectionTitle);
    }

    @Transactional
    public void deleteCollection(String userId, Long collectionId) {
        QnaCollection collectionById = qnaService.findCollectionById(collectionId);

        isCollectionWriter(userId, collectionById);

        if (!collectionById.getQnaBookmark().isEmpty()) {
            throw new CollectionNotEmptyException();
        }

        qnaCollectionRepository.delete(collectionById);
    }

    private void isCollectionWriter(String userId, QnaCollection collectionById) {
        if (!Objects.equals(collectionById.getUser().getUuid().toString(), userId)) {
            throw new NotCollectionWriterException();
        }
    }
}
