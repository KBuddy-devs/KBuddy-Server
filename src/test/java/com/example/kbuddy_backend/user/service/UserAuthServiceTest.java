package com.example.kbuddy_backend.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.kbuddy_backend.auth.dto.response.AccessTokenAndRefreshTokenResponse;
import com.example.kbuddy_backend.common.IntegrationTest;
import com.example.kbuddy_backend.fixtures.UserFixtures;
import com.example.kbuddy_backend.user.dto.request.LoginRequest;
import com.example.kbuddy_backend.user.entity.User;
import com.example.kbuddy_backend.user.exception.DuplicateUserException;
import com.example.kbuddy_backend.user.exception.InvalidPasswordException;
import com.example.kbuddy_backend.user.exception.UserNotFoundException;
import com.example.kbuddy_backend.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;


class UserAuthServiceTest extends IntegrationTest {

    @Autowired
    private UserAuthService userAuthService;

    @MockBean
    private UserRepository userRepository;
    @Mock
    private LoginRequest mockLoginRequest;

    @DisplayName("회원가입 성공시 토큰을 반환한다.")
    @Test
    void loginSuccess() {
        //given
        LoginRequest loginRequest = LoginRequest.of("test", "test", "test");
        given(userRepository.save(any(User.class))).willReturn(UserFixtures.createUser());

        //when
        AccessTokenAndRefreshTokenResponse register = userAuthService.register(loginRequest);

        //then
        assertThat(register).extracting("accessToken").isNotNull();
        assertThat(register).extracting("refreshToken").isNotNull();
     }

    @DisplayName("패스워드가 일치하지 않으면 에러를 던진다.")
    @Test
    void checkInvalidPassword() {
        //given
        User user = UserFixtures.createUser();
        LoginRequest loginRequest = LoginRequest.of("test", "test", "test");
        given(mockLoginRequest.password()).willReturn("invalidPassword");
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));

        //then
        assertThatThrownBy(() -> userAuthService.login(loginRequest))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다.");
    }

    
    @DisplayName("아이디가 일치하지 않으면 에러를 던진다.")
    @Test
    void checkNotFoundUser() {
        //given
        LoginRequest loginRequest = LoginRequest.of("test", "test", "test");
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
        
        //then
        assertThatThrownBy(() -> userAuthService.login(loginRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다.");
     }
     
     @DisplayName("중복된 이메일을 확인한다.")
     @Test
     void checkDuplicatedEmail() {
         //given
         User user = UserFixtures.createUser();
         LoginRequest loginRequest = LoginRequest.of("test", user.getEmail(), "test");
         given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));

         //then
         assertThatThrownBy(() -> userAuthService.register(loginRequest))
                 .isInstanceOf(DuplicateUserException.class)
                 .hasMessageContaining("이미 존재하는 사용자입니다.");
      }
}