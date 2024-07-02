package com.example.kbuddy_backend.user.service;

import com.example.kbuddy_backend.user.dto.response.UserResponse;
import com.example.kbuddy_backend.user.entity.User;
import com.example.kbuddy_backend.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUser(User user) {
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        return UserResponse.of(findUser.getId(), findUser.getUsername(), findUser.getEmail(), findUser.getProfileImageUrl(), findUser.getBio(), findUser.getCreatedDate(), findUser.getFirstName(), findUser.getLastName());
    }
}
