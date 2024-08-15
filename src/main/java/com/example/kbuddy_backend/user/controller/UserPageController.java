package com.example.kbuddy_backend.user.controller;

import com.example.kbuddy_backend.common.config.CurrentUser;
import com.example.kbuddy_backend.user.dto.request.CollectionRequest;
import com.example.kbuddy_backend.user.dto.request.UserProfileUpdateRequest;
import com.example.kbuddy_backend.user.dto.response.AllUserResponse;
import com.example.kbuddy_backend.user.dto.response.UserResponse;
import com.example.kbuddy_backend.user.entity.User;
import com.example.kbuddy_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        UserResponse userResponse = userService.getUser(user);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<AllUserResponse> getAllUsers(
            @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        AllUserResponse allUsers = userService.getAllUsers(pageable);
        return ResponseEntity.ok().body(allUsers);
    }

    //단일 유저 정보 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UserProfileUpdateRequest request,
                                                @PathVariable String userId) {
        UserResponse userResponse = userService.updateProfile(request, userId);
        return ResponseEntity.ok().body(userResponse);
    }

    //단일 유저 계정 비활성화
    @PostMapping("/{userId}/resign")
    public ResponseEntity<String> resign(@PathVariable String userId) {
        userService.resign(userId);
        return ResponseEntity.ok().body("계정 비활성화가 완료되었습니다. 30일 이내 활성화 신청을 하지 않으면 계정이 삭제됩니다.");
    }

    //단일 유저 계정 활성화
    @PostMapping("/{userId}/assign")
    public ResponseEntity<UserResponse> assign(@PathVariable String userId) {
        UserResponse userResponse = userService.assign(userId);
        return ResponseEntity.ok().body(userResponse);
    }

    //단일 유저 컬렉션 생성
    @PostMapping("/collection")
    public ResponseEntity<String> saveUserCollection(@RequestBody CollectionRequest collectionRequest,
                                                     @CurrentUser User user) {

        userService.saveCollection(collectionRequest, user);
        return ResponseEntity.ok().body("성공적으로 저장되었습니다.");

    }

    //단일 유저 컬렉션 수정
    @PatchMapping("/{userId}/collection/{collectionId}")
    public ResponseEntity<String> updateCollectionName(@PathVariable final String userId,
                                                       @PathVariable final Long collectionId,
                                                       @RequestBody CollectionRequest collectionRequest) {
        //todo : 컬렉션 목록 반환
        userService.updateCollection(userId, collectionId, collectionRequest);
        return ResponseEntity.ok().body("성공적으로 수정되었습니다.");
    }

    //단일 유저 컬렉션 삭제
    @DeleteMapping("/{userId}/collection/{collectionId}/delete")
    public ResponseEntity<Void> deleteCollection(@PathVariable final String userId,
                                                 @PathVariable final Long collectionId) {
        userService.deleteCollection(userId, collectionId);
        return ResponseEntity.noContent().build();
    }
}
