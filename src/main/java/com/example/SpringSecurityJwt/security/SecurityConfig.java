package com.example.SpringSecurityJwt.security;

import com.example.SpringSecurityJwt.security.filters.JwtAuthenticationFilter;
import com.example.SpringSecurityJwt.security.filters.JwtAuthorizationFilter;
import com.example.SpringSecurityJwt.security.jwt.JwtUtils;
import com.example.SpringSecurityJwt.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//@EnableGlobalMethodSecurity(prePostEnabled = true)-->habilitar las anotaciones para roles de spring security para
// nuestros controladores que lo usaran.En este caso lo usa "TestRolesController".Sino usas esta anotación usarias
//el convencional que es -> auth.requestMatchers("/example").hasRole("ADMIN");
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    JwtAuthorizationFilter authorizationFilter;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    //configurando la cadena de filtros(la seguridd)
    //Configurando el acceso a nuestros endpoints y manejo de sesion
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,AuthenticationManager authenticationManager) throws Exception {
        //obligatorio para agregar el filtro de autenticacion
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login"); // URL para la autenticación

        return httpSecurity
                .csrf(config -> config.disable())//opcional.Si no vas a trabajar con formulario usa esto sino no.
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/hello","/createUser").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> {//no vamos a manejar una sesion directamente
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                //primero se valida la autenticacion y luego se genera el token .Ahora trabajo con json ya no con un login q era httpBasic
                .addFilter(jwtAuthenticationFilter)// Agrega el filtro de autenticación JWT
                // Agrega el filtro de autorización JWT antes del filtro de autenticación de usuario y contraseña
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    //Encriptando la constraseña
    //configurando la politica de encriptacion de contraseñas
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //Para q el usuario pueda funcionar debe ser administrado por el AuthenticationManagaer
    //AuthenticationManager-->Administra la autenticacion en la aplicacion,exige manejar un passwordEncoder
    //Administracion de la autenticacion de los usuarios
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity,PasswordEncoder passwordEncoder) throws Exception{
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                //Pasandole el usuario q vamos a autenticar
                   .userDetailsService(userDetailsService)
                //enviando el passwordEncoder
                .passwordEncoder(passwordEncoder())
                .and().build();
    }

}
