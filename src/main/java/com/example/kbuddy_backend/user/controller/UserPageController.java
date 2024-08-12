package com.example.kbuddy_backend.user.controller;

import com.example.kbuddy_backend.common.config.CurrentUser;
import com.example.kbuddy_backend.user.dto.request.CollectionRequest;
import com.example.kbuddy_backend.user.dto.request.UserBioRequest;
import com.example.kbuddy_backend.user.dto.response.AllUserResponse;
import com.example.kbuddy_backend.user.dto.response.UserResponse;
import com.example.kbuddy_backend.user.entity.User;
import com.example.kbuddy_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kbuddy/v1/user")
public class UserPageController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@CurrentUser User user) {

        UserResponse findUser = userService.getUser(user);
        return ResponseEntity.ok().body(findUser);
    }

    @GetMapping("/all")
    public ResponseEntity<AllUserResponse> getAllUsers(@PageableDefault(size = 5,sort = "createdDate",direction = Sort.Direction.DESC) Pageable pageable) {
        AllUserResponse allUsers = userService.getAllUsers(pageable);
        return ResponseEntity.ok().body(allUsers);
    }

    @PostMapping("/profile/images")
    public ResponseEntity<String> uploadProfileImage(@RequestPart MultipartFile image, @CurrentUser User user) {
        userService.uploadProfileImage(image, user);
        return ResponseEntity.ok().body("프로필 사진이 성공적으로 저장되었습니다.");
    }

    @PostMapping("/bio")
    public ResponseEntity<String> saveUserBio(@RequestBody UserBioRequest request, @CurrentUser User user) {
        userService.saveUserBio(request, user);
        return ResponseEntity.ok().body("성공적으로 저장되었습니다.");
    }

    //단일 유저 컬렉션 생성
    @PostMapping("/collection")
    public ResponseEntity<String> saveUserCollection(@RequestBody CollectionRequest collectionRequest,
                                                     @CurrentUser User user) {
        userService.saveCollection(collectionRequest, user);
        return ResponseEntity.ok().body("성공적으로 저장되었습니다.");

    }
}
