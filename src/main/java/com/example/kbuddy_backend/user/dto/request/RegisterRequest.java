package com.example.kbuddy_backend.user.dto.request;

import com.example.kbuddy_backend.user.constant.Country;
import com.example.kbuddy_backend.user.constant.Gender;

import jakarta.validation.constraints.Email;

//todo: validation
public record RegisterRequest(String userId, @Email String email, String password, String firstName, String lastName,
							  Country country, Gender gender) {
	public static RegisterRequest of(String userId, String email, String password, String firstName, String lastName,
		Country country, Gender gender) {
		return new RegisterRequest(userId, email, password, firstName, lastName, country, gender);
	}
}