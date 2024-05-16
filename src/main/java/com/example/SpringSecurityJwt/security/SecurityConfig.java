package com.example.SpringSecurityJwt.security;

import com.example.SpringSecurityJwt.security.filters.JwtTokenValidator;
import com.example.SpringSecurityJwt.service.impl.UserDetailsServiceImpl;
import com.example.SpringSecurityJwt.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


//@EnableGlobalMethodSecurity(prePostEnabled = true)-->habilitar las anotaciones para roles de spring security para
// nuestros controladores que lo usaran.En este caso lo usa "TestRolesController".Sino usas esta anotación usarias
//el convencional que es -> auth.requestMatchers("/example").hasRole("ADMIN");
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailService;

    //configurando la cadena de filtros(la seguridd)
    //Configurando el acceso a nuestros endpoints y manejo de sesion
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationProvider authenticationProvider) throws Exception {//obligatorio para agregar el filtro de autenticacion

        return httpSecurity
                .csrf(config -> config.disable())//opcional.Si no vas a trabajar con formulario usa esto sino no.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//no vamos a manejar una sesion directamente
                .authorizeHttpRequests(http -> {
                    http.requestMatchers("/hello","/createUser").permitAll();
                    http.anyRequest().denyAll();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                // Agrega el filtro de autorización JWT antes del filtro de autenticación de usuario y contraseña
                .addFilterBefore(new JwtTokenValidator(jwtUtils,userDetailService), BasicAuthenticationFilter.class)
                .build();
    }


    //Encriptando la constraseña
    //configurando la politica de encriptacion de contraseñas
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //    Administra la autenticacion
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Permite conectarnos a la bd usando el passwordencoder y userdetailsService
// El UserDetailsService es el q se conecta a la bd para verificar la autenticacion
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

}
