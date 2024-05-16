package com.example.SpringSecurityJwt.service;

import com.example.SpringSecurityJwt.dto.user.AuthLoginRequest;
import com.example.SpringSecurityJwt.dto.user.AuthResponse;
import com.example.SpringSecurityJwt.dto.user.CreateUserDTO;

public interface UserEntityService {

    CreateUserDTO createUser(CreateUserDTO createUserDTO);

    AuthResponse loginUser(AuthLoginRequest userRequest);
}
