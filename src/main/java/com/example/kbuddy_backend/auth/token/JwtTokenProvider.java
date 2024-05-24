package com.example.kbuddy_backend.auth.token;

import com.example.kbuddy_backend.auth.dto.response.TokenResponse;
import com.example.kbuddy_backend.auth.exception.TokenExpirationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config.properties")
public class JwtTokenProvider implements TokenProvider {

    private final SecretKey key;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    public JwtTokenProvider(@Value("${security.jwt.secret-key}") final String secretKey,
                            @Value("${security.jwt.access-token-expiration}") final long accessTokenValidityInMilliseconds,
                            @Value("${security.jwt.refresh-token-expiration}") final long refreshTokenValidityInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;

    }

    @Override
    public TokenResponse createToken(final String email, final String role, final long tokenValidityInMilliseconds) {

        Claims claims = Jwts.claims();
        //상담원 사용자인지 일반 사용자인지 구분하기 위한 role 추가
        claims.put("role", role);

        String token = Jwts.builder()
                .setSubject(email)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenResponse.from(token);
    }

    public TokenResponse createAccessToken(final String email, final String role) {
        return createToken(email, role, accessTokenValidityInMilliseconds);
    }

    public TokenResponse createRefreshToken(final String email, final String role) {
        return createToken(email, role, refreshTokenValidityInMilliseconds);
    }


    @Override
    public String getPayload(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public boolean validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            Date tokenExpirationDate = claims.getBody().getExpiration();
            validateTokenExpiration(tokenExpirationDate);
            return true;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private void validateTokenExpiration(Date tokenExpirationDate) {
        if (tokenExpirationDate.before(new Date(System.currentTimeMillis()))) {
            throw new TokenExpirationException();
        }
    }

    @Override
    public long getAccessTokenExpiryDuration() {
        return accessTokenValidityInMilliseconds;
    }

    @Override
    public long getRefreshTokenExpiryDuration() {
        return refreshTokenValidityInMilliseconds;
    }

    //사용자 role 반환
    public String getUserRole(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
