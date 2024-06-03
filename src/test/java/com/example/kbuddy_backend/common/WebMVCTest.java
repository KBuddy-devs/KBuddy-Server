package com.example.kbuddy_backend.common;

import com.example.kbuddy_backend.common.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@Import(SecurityConfig.class)
public abstract class WebMVCTest {

    @Autowired
    MockMvc mockMvc;
}
