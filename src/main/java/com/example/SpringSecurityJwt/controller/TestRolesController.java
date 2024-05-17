package com.example.SpringSecurityJwt.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Restringiendo el acceso a algunos endpoint de nuestra aplicacion
@RestController
@RequestMapping("/test")
public class TestRolesController {

    @GetMapping("/Admin")
    public String accessAdmin(){
        return "Hola,has accedido con rol de ADMIN";
    }

    @GetMapping("/User")
    public String accessUser(){
        return "Hola, has accedido con rol de USER";
    }

}
