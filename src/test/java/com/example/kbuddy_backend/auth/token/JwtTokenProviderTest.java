package com.example.kbuddy_backend.auth.token;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.kbuddy_backend.auth.dto.response.TokenResponse;
import com.example.kbuddy_backend.common.WebMVCTest;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

class JwtTokenProviderTest extends WebMVCTest {

    @DisplayName("만료된 토큰인지 확인한다.")
    @Test
    void isExpiredToken() {

        TokenResponse invalidTokens = createInvalidTokens();
        //then
        assertThrows(ExpiredJwtException.class, () -> {
            tokenProvider.validateToken(invalidTokens.token());
        });
    }

    @DisplayName("유효한 토큰을 검증한다.")
    @Test
    void isValidatedToken() {
        //given

        //when
        TokenResponse token = createTokens();

        //then
        assertDoesNotThrow(() -> tokenProvider.validateToken(token.token()));

    }


    @DisplayName("유효하지 않은 토큰을 검증한다.")
    @Test
    void isUnValidatedToken() {
        //given
        TokenResponse token = createTokens();
        String inValidatedToken = token.token() + "invalid";

        //then
        assertThrows(JwtException.class, () -> {
            tokenProvider.validateToken(inValidatedToken);
        });
    }

    @DisplayName("토큰에 포함된 인증 정보를 확인한다.")
    @Test
    void checkUserAuthenticationInToken() {
        //given
        TokenResponse tokens = createTokens();

        //when
        Authentication authentication = tokenProvider.getAuthentication(tokens.token());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        //then
        assertThat(authentication.getName()).isEqualTo("email");
        assertThat(authorities).hasSize(1).extracting(GrantedAuthority::getAuthority).contains("ROLE_USER");
    }

    private TokenResponse createTokens() {

        final List<GrantedAuthority> grantedAuthorities = List.of(() -> "ROLE_USER");
        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("email", "password", grantedAuthorities);
        return tokenProvider.createToken(authenticationToken, 3600);
    }

    private TokenResponse createInvalidTokens() {

        final List<GrantedAuthority> grantedAuthorities = List.of(() -> "ROLE_USER");
        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("email", "password", grantedAuthorities);
        return tokenProvider.createToken(authenticationToken, 0);
    }
}
