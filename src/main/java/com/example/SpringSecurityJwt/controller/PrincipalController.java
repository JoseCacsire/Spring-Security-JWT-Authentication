package com.example.SpringSecurityJwt.controller;

import com.example.SpringSecurityJwt.models.ERole;
import com.example.SpringSecurityJwt.models.RoleEntity;
import com.example.SpringSecurityJwt.models.UserEntity;
import com.example.SpringSecurityJwt.repositories.UserRepository;
import com.example.SpringSecurityJwt.request.CreateUserDTO;
import com.example.SpringSecurityJwt.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PrincipalController {

    //para encriptar el password
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello(){
        return "Hello World Not Secured";
    }

    @GetMapping("/helloSecured")
    public String helloSecured(){
        return "Hello World  Secured";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO){
        //USO @VALID para q se valide q los campos sean correctos como q el email no sea vacio
        log.error("createUser");
        try {
            UserEntity userEntity = userService.createUser(createUserDTO);
            return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String id){
        //pasarlo a long ya q en mi dao defini mi id como tipo Long
        userService.deleteById(Long.parseLong(id));
        return "Se ha borrado el user con id".concat(id);

    }


}
