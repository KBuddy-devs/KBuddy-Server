package com.example.kbuddy_backend.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.kbuddy_backend.common.WebMVCTest;
import com.example.kbuddy_backend.user.dto.request.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserAuthControllerTest extends WebMVCTest {


    @DisplayName("로그인 성공 테스트")
    @Test
    void loginSuccess() throws Exception {
        //given
        LoginRequest loginRequest = LoginRequest.of("test", "test", "test");

        //when
        
        //then
        mockMvc.perform(post("/api/v1/user/auth/login").content(objectMapper.writeValueAsString(loginRequest))
                .contentType(APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
     }
}