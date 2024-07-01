package com.example.kbuddy_backend.user.controller;

import com.example.kbuddy_backend.auth.dto.response.AccessTokenAndRefreshTokenResponse;
import com.example.kbuddy_backend.user.dto.request.LoginRequest;
import com.example.kbuddy_backend.user.dto.response.UserResponse;
import com.example.kbuddy_backend.user.entity.User;
import com.example.kbuddy_backend.user.repository.UserRepository;
import com.example.kbuddy_backend.user.service.UserAuthService;
import java.util.List;
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
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AccessTokenAndRefreshTokenResponse> register(@RequestBody final LoginRequest loginRequest) {
        AccessTokenAndRefreshTokenResponse token = userAuthService.register(loginRequest);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody final LoginRequest loginRequest) {
        UserResponse userResponse = userAuthService.login(loginRequest);

        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUser() {
        return ResponseEntity.ok().body(userRepository.findAll());
    }

    //테스트 api
    @GetMapping("/authentication")
    public ResponseEntity<Authentication> getUserAuthentication(Authentication authentication) {

        return ResponseEntity.ok().body(authentication);
    }
}