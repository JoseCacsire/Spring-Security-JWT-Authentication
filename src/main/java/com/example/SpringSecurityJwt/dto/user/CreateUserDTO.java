package com.example.SpringSecurityJwt.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(
        @NotBlank
        String username,
        @Email
        @NotBlank
        String email,
        String password
) {
}
