package com.example.kbuddy_backend.user.dto.response;

import com.example.kbuddy_backend.common.dto.ImageFileDto;
import com.example.kbuddy_backend.user.constant.Country;
import com.example.kbuddy_backend.user.constant.Gender;
import com.example.kbuddy_backend.user.entity.Authority;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(String id, String userId, String email, List<String> roles, ImageFileDto profileImage,
						   String bio, String firstName,
						   String lastName,
						   Gender gender, Country country, boolean isActive, LocalDateTime createdDate) {

	public static UserResponse of(String id, String userId, String email, List<String> roles, ImageFileDto profileImage,
		String bio,
		String firstName, String lastName, LocalDateTime createdDate, Gender gender,
		Country country, boolean isActive) {
		return new UserResponse(id, userId, email, roles, profileImage, bio, firstName, lastName, gender, country,
			isActive,
			createdDate);
	}
}
