package com.example.kbuddy_backend.user.dto.request;

public record UsernameRequest(String username) {
	public static UsernameRequest of(String username) {
		return new UsernameRequest(username);
	}
}
