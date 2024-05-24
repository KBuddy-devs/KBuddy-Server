package com.example.kbuddy_backend.auth.token;

import com.example.kbuddy_backend.auth.dto.response.TokenResponse;

public interface TokenProvider {

    TokenResponse createToken(final String email, final String role,final long tokenValidityInMilliseconds);

    String getPayload(final String token);

    boolean validateToken(final String token);

    long getAccessTokenExpiryDuration();
    long getRefreshTokenExpiryDuration();
}
