package com.example.kbuddy_backend.user.dto.request;

import com.example.kbuddy_backend.user.constant.Country;
import com.example.kbuddy_backend.user.constant.Gender;
import com.example.kbuddy_backend.user.constant.OAuthCategory;
import jakarta.validation.constraints.Email;

//todo: validation
public record OAuthRegisterRequest(String userId, @Email String email, String password, String firstName, String lastName,
								   Country country, Gender gender, OAuthCategory oAuthCategory) {
	public static OAuthRegisterRequest of(String userId, String email, String password, String firstName, String lastName,
                                          Country country, Gender gender, OAuthCategory oAuthCategory) {
		return new OAuthRegisterRequest(userId, email, password, firstName, lastName, country, gender, oAuthCategory);
	}
}