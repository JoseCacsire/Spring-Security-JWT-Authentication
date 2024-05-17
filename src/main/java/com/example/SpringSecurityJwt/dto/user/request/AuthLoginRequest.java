package com.example.SpringSecurityJwt.dto.user.request;


import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String username,
                               @NotBlank String password) {
}