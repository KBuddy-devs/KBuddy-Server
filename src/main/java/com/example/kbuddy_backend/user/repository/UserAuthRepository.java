package com.example.kbuddy_backend.user.repository;

import com.example.kbuddy_backend.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
