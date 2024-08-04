package com.example.kbuddy_backend.common.config;

import java.util.Arrays;
import java.util.List;

import com.example.kbuddy_backend.auth.config.JwtAccessDeniedHandler;
import com.example.kbuddy_backend.auth.config.JwtAuthenticationEntryPoint;
import com.example.kbuddy_backend.auth.config.JwtFilter;
import com.example.kbuddy_backend.auth.token.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final JwtTokenProvider tokenProvider;

	//시큐리티를 적용하지 않을 리소스
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers(getPermitAllEndpoints().toArray(String[]::new));
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(sessionManagement -> sessionManagement
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
			.addFilterBefore(new JwtFilter(tokenProvider, getPermitAllEndpoints()),
				UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exceptionHandling -> {
				exceptionHandling
					.authenticationEntryPoint(jwtAuthenticationEntryPoint)
					.accessDeniedHandler(jwtAccessDeniedHandler);
			})
			.authorizeHttpRequests(authorizeRequests ->
				authorizeRequests
					.requestMatchers("/kbuddy/v1/user/auth/password", "/kbuddy/v1/user/auth/authentication")
					.authenticated()
					.requestMatchers("/kbuddy/v1/user/auth/**")
					.permitAll()
					.requestMatchers("/kbuddy/v1/user/**")
					.authenticated()
					.requestMatchers("/**")
					.permitAll());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//허용 엔드포인트 리스트
	private List<String> getPermitAllEndpoints() {
		return Arrays.asList(
			"/error",
			"/favicon.ico",
			"/h2-console/**",
			"/api-docs/**",
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/index.html",
			"/kbuddy/v1/user/auth/**"
		);
	}

}


