package com.example.kbuddy_backend.user.service;

import static com.example.kbuddy_backend.user.constant.UserRole.BUISNESS_USER;

import com.example.kbuddy_backend.auth.dto.response.AccessTokenAndRefreshTokenResponse;
import com.example.kbuddy_backend.auth.service.AuthService;
import com.example.kbuddy_backend.user.dto.request.LoginRequest;
import com.example.kbuddy_backend.user.dto.response.UserResponse;
import com.example.kbuddy_backend.user.entity.User;
import com.example.kbuddy_backend.user.exception.DuplicateUserException;
import com.example.kbuddy_backend.user.exception.InvalidPasswordException;
import com.example.kbuddy_backend.user.exception.UserNotFoundException;
import com.example.kbuddy_backend.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Transactional
    public AccessTokenAndRefreshTokenResponse register(final LoginRequest loginRequest) {

        final String email = loginRequest.email();

        final Optional<User> user = userRepository.findByUsername(email);

        if (user.isPresent()) {
            throw new DuplicateUserException();
        }

        final String password = passwordEncoder.encode(loginRequest.password());

        final User newUser = User.builder()
                .username(email)
                .password(password).build();
        User saveUser = userRepository.save(newUser);

        List<GrantedAuthority> grantedAuthorities = saveUser.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, password,grantedAuthorities);

        return authService.createToken(authentication);
    }

    public UserResponse login(final LoginRequest loginRequest) {

        final String username = loginRequest.username();
        final String password = loginRequest.password();

        final Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        final User findUser = user.get();

        if (!passwordEncoder.matches(password, findUser.getPassword())) {
            throw new InvalidPasswordException();
        }

        return UserResponse.of(findUser.getId(), findUser.getUsername());
    }
}
