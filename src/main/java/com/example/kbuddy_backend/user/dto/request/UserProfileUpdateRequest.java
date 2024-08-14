package com.example.kbuddy_backend.user.dto.request;

import com.example.kbuddy_backend.common.dto.ImageFileDto;
import com.example.kbuddy_backend.user.constant.Country;
import com.example.kbuddy_backend.user.constant.Gender;

public record UserProfileUpdateRequest(String email, String userId, Country country, Gender gender, String bio, ImageFileDto profileImage, String firstName,
                                       String lastName) {

}
