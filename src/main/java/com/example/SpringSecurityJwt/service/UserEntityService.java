package com.example.SpringSecurityJwt.service;

import com.example.SpringSecurityJwt.dto.user.request.AuthLoginRequest;
import com.example.SpringSecurityJwt.dto.user.response.AuthResponse;
import com.example.SpringSecurityJwt.dto.user.request.CreateUserDTO;

public interface UserEntityService {

    CreateUserDTO createUser(CreateUserDTO createUserDTO);

    AuthResponse loginUser(AuthLoginRequest userRequest);
}
