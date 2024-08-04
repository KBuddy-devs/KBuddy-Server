package com.example.kbuddy_backend.auth.config;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import com.example.kbuddy_backend.auth.exception.TokenNotFoundException;
import com.example.kbuddy_backend.auth.token.JwtTokenProvider;
import com.example.kbuddy_backend.common.advice.response.ApiResponse;
import com.example.kbuddy_backend.common.advice.response.CustomCode;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	private final JwtTokenProvider jwtTokenProvider;
	private final List<String> permitAllEndpoints;

	//토큰의 인증정보를 SecurityContext에 저장하는 역할
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		final HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		final HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		final String token = resolveToken(httpServletRequest);
		final String requestURI = httpServletRequest.getRequestURI();
		String errorMessage;

		// if (!requestURI.equals("/kbuddy/v1/user/auth/password") && !requestURI.equals(
		// 	"/kbuddy/v1/user/auth/authentication") && isPermitAllEndpoint(requestURI)) {
		// 	chain.doFilter(request, response);
		// 	return;
		// }

		try {
			if (token == null) {
				throw new TokenNotFoundException();
			}
			jwtTokenProvider.validateToken(token);
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authentication);
			log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
			chain.doFilter(request, response);
			return;

		} catch (ExpiredJwtException e) {
			log.info("토큰이 만료되었습니다. uri: {}", requestURI);
			errorMessage = "토큰이 만료되었습니다.";
		} catch (TokenNotFoundException e) {
			log.info("토큰이 존재하지 않습니다. uri: {}", requestURI);
			errorMessage = "토큰이 존재하지 않습니다.";
		} catch (JwtException | IllegalArgumentException e) {
			log.info("유효하지 않은 토큰입니다. uri: {}", requestURI);
			errorMessage = "유효하지 않은 토큰입니다.";
		} catch (Exception e) {
			log.info("JWT 검증 중에 에외가 발생했습니다. uri: {}", requestURI);
			errorMessage = "JWT 검증 중에 에외가 발생했습니다.";
		}
		httpServletResponse.setStatus(SC_UNAUTHORIZED);
		httpServletResponse.setContentType("application/json;charset=UTF-8");
		httpServletResponse.getWriter().write(
			ApiResponse.error(errorMessage, httpServletRequest.getRequestURI(), 401, CustomCode.HTTP_401.getCode())
				.toJson());
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	// private boolean isPermitAllEndpoint(String requestURI) {
	// 	return permitAllEndpoints.stream().anyMatch(requestURI::startsWith);
	// }
}
