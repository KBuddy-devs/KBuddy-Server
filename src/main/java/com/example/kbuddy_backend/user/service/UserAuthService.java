package com.example.kbuddy_backend.user.service;

import com.example.kbuddy_backend.user.dto.request.LoginRequest;
import com.example.kbuddy_backend.user.dto.response.UserResponse;
import com.example.kbuddy_backend.user.entity.User;
import com.example.kbuddy_backend.user.exception.DuplicateUserException;
import com.example.kbuddy_backend.user.exception.InvalidPasswordException;
import com.example.kbuddy_backend.user.exception.UserNotFoundException;
import com.example.kbuddy_backend.user.repository.UserAuthRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(final LoginRequest loginRequest) {

        String username = loginRequest.username();

        final Optional<User> user = userAuthRepository.findByUsername(username);

        if (user.isPresent()) {
            throw new DuplicateUserException();
        }

        String password = passwordEncoder.encode(loginRequest.password());

        User newUser = User.builder()
                .username(username)
                .password(password).build();
        User saveUser = userAuthRepository.save(newUser);

        return UserResponse.of(saveUser.getId(), saveUser.getUsername());
    }

    public UserResponse login(final LoginRequest loginRequest) {

        String username = loginRequest.username();
        String password = loginRequest.password();

        final Optional<User> user = userAuthRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        User findUser = user.get();

        if (!passwordEncoder.matches(password, findUser.getPassword())) {
            throw new InvalidPasswordException();
        }

        return UserResponse.of(findUser.getId(), findUser.getUsername());
    }
}
