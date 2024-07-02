package com.example.kbuddy_backend.user.controller;

import com.example.kbuddy_backend.auth.dto.response.AccessTokenAndRefreshTokenResponse;
import com.example.kbuddy_backend.common.config.CurrentUser;
import com.example.kbuddy_backend.user.dto.request.EmailRequest;
import com.example.kbuddy_backend.user.dto.request.LoginRequest;
import com.example.kbuddy_backend.user.dto.request.RegisterRequest;
import com.example.kbuddy_backend.user.dto.response.UserResponse;
import com.example.kbuddy_backend.user.entity.User;
import com.example.kbuddy_backend.user.repository.UserRepository;
import com.example.kbuddy_backend.user.service.UserAuthService;
import com.example.kbuddy_backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/auth")
public class UserAuthController {

    private final UserAuthService userAuthService;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AccessTokenAndRefreshTokenResponse> register(@RequestBody final RegisterRequest registerRequest) {
        AccessTokenAndRefreshTokenResponse token = userAuthService.register(registerRequest);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody final LoginRequest loginRequest) {
        UserResponse userResponse = userAuthService.login(loginRequest);

        return ResponseEntity.ok().body(userResponse);
    }

    //토큰 불필요
    @PostMapping("/check/email")
    public ResponseEntity<String> checkEmail(@Valid @RequestBody final EmailRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.ok().body("이메일이 존재합니다.");
        }
        return ResponseEntity.ok().body("사용자가 없습니다.");
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@CurrentUser User user) {

        UserResponse findUser = userService.getUser(user);
        return ResponseEntity.ok().body(findUser);
    }

    //테스트 api
    @GetMapping("/authentication")
    public ResponseEntity<Authentication> getUserAuthentication(Authentication authentication) {

        return ResponseEntity.ok().body(authentication);
    }
}