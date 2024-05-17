package com.example.SpringSecurityJwt.controller;

import com.example.SpringSecurityJwt.dto.user.request.AuthLoginRequest;
import com.example.SpringSecurityJwt.dto.user.response.AuthResponse;
import com.example.SpringSecurityJwt.dto.user.request.CreateUserDTO;
import com.example.SpringSecurityJwt.service.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<?> login(@RequestBody @Valid AuthLoginRequest userRequest){
        try{
            return new ResponseEntity<>(userEntityService.loginUser(userRequest), HttpStatus.OK);
        }catch (Exception e){
           return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
    }

}
