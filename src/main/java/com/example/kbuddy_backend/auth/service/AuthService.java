package com.example.kbuddy_backend.auth.service;

import com.example.kbuddy_backend.auth.dto.AccessTokenAndRefreshTokenResponse;
import com.example.kbuddy_backend.auth.dto.response.TokenResponse;
import com.example.kbuddy_backend.auth.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AccessTokenAndRefreshTokenResponse createToken(final String email, final String role) {

        final TokenResponse accessToken = jwtTokenProvider.createAccessToken(email, role);
        final TokenResponse refreshToken = jwtTokenProvider.createRefreshToken(email, role);

        return AccessTokenAndRefreshTokenResponse.of(accessToken.token(), refreshToken.token(),
                jwtTokenProvider.getAccessTokenExpiryDuration(), jwtTokenProvider.getRefreshTokenExpiryDuration());
    }

}
