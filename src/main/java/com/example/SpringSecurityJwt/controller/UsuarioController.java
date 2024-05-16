package com.example.SpringSecurityJwt.controller;

import com.example.SpringSecurityJwt.dto.user.AuthLoginRequest;
import com.example.SpringSecurityJwt.dto.user.AuthResponse;
import com.example.SpringSecurityJwt.dto.user.CreateUserDTO;
import com.example.SpringSecurityJwt.service.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")

public class UsuarioController {

    @Autowired
    private UserEntityService userEntityService;

    @PostMapping("/register")
    public ResponseEntity<CreateUserDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        CreateUserDTO createdUser = userEntityService.createUser(createUserDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(userEntityService.loginUser(userRequest), HttpStatus.OK);
    }

}
