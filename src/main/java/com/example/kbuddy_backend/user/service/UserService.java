package com.example.kbuddy_backend.user.service;

import com.example.kbuddy_backend.s3.dto.response.S3Response;
import com.example.kbuddy_backend.s3.exception.ImageUploadException;
import com.example.kbuddy_backend.s3.service.S3Service;
import com.example.kbuddy_backend.user.dto.request.UserBioRequest;
import com.example.kbuddy_backend.user.dto.response.AllUserResponse;
import com.example.kbuddy_backend.user.dto.response.UserResponse;
import com.example.kbuddy_backend.user.entity.User;
import com.example.kbuddy_backend.user.entity.UserImage;
import com.example.kbuddy_backend.user.repository.UserImageRepository;
import com.example.kbuddy_backend.user.repository.UserRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

	private final UserRepository userRepository;
	private final S3Service s3Service;
	private static final String FOLDER_NAME = "user";
	private final UserImageRepository userImageRepository;

	public UserResponse getUser(User user) {
		User findUser = userRepository.findById(user.getId())
			.orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
		return createUserDto(findUser);
	}

	public AllUserResponse getAllUsers(Pageable pageable) {
		Page<User> allUsers = userRepository.findAll(pageable);
		return AllUserResponse.of(allUsers.getTotalElements(), allUsers.getTotalPages(), allUsers.getNumber(),
			allUsers.stream().map(user -> createUserDto(user)).toList()
		);
	}

	@Transactional
	public void uploadProfileImage(MultipartFile image, User user) {
		S3Response s3Response = storeImage(image);

		//프로필 사진을 변경하는 경우 기존 사진을 삭제
		if (user.getImageUrls() != null) {
			s3Service.deleteFile(user.getImageUrls().getFilePath());
			user.getImageUrls().update(s3Response.s3ImageUrl(), s3Response.originalFileName(), s3Response.filePath());
			return;
		}

		UserImage userImage = UserImage.builder()
			.user(user)
			.imageUrl(s3Response.s3ImageUrl())
			.filePath(s3Response.filePath())
			.originalFileName(s3Response.originalFileName())
			.build();
		user.setImageUrls(userImage);
	}

	private S3Response storeImage(MultipartFile image) {
		if (!s3Service.checkImageFile(image) && image != null && !image.isEmpty()) {
			throw new ImageUploadException("이미지 파일이 아닙니다.");
		}
		return s3Service.saveFileWithUUID(image, FOLDER_NAME);
	}

	@Transactional
	public void saveUserBio(UserBioRequest request, User user) {
		user.updateBio(request.bio());
	}

	private UserResponse createUserDto(User findUser) {
		List<String> authorities = findUser.getAuthorities().stream()
			.map(authority -> authority.getAuthorityName().name())
			.toList();
		return UserResponse.of(findUser.getUuid().toString(), findUser.getUsername(), findUser.getEmail(), authorities,
			findUser.getImageUrls() == null ? null : findUser.getImageUrls().getImageUrl(),
			findUser.getBio(), findUser.getFirstName(), findUser.getLastName(),
			findUser.getCreatedDate(), findUser.getGender(), findUser.getCountry(), findUser.isActive());
	}

}
