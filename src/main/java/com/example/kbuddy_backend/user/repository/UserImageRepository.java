package com.example.kbuddy_backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kbuddy_backend.user.entity.UserImage;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
}
