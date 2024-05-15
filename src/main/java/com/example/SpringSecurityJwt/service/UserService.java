package com.example.SpringSecurityJwt.service;

import com.example.SpringSecurityJwt.models.UserEntity;
import com.example.SpringSecurityJwt.request.CreateUserDTO;

public interface UserService {

    UserEntity createUser(CreateUserDTO createUserDTO);
    void deleteById(long l);

}
