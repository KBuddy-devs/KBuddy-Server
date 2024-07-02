package com.example.kbuddy_backend.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record EmailRequest(@NotNull String email) {
}
