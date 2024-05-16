package com.example.SpringSecurityJwt.service.impl;

import com.example.SpringSecurityJwt.dto.user.AuthLoginRequest;
import com.example.SpringSecurityJwt.dto.user.AuthResponse;
import com.example.SpringSecurityJwt.dto.user.CreateUserDTO;
import com.example.SpringSecurityJwt.models.ERole;
import com.example.SpringSecurityJwt.models.RoleEntity;
import com.example.SpringSecurityJwt.models.UserEntity;
import com.example.SpringSecurityJwt.repositories.RoleRepository;
import com.example.SpringSecurityJwt.repositories.UserRepository;
import com.example.SpringSecurityJwt.service.UserEntityService;
import com.example.SpringSecurityJwt.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserEntityService {


    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    @Transactional
    public CreateUserDTO createUser(CreateUserDTO createUserDTO) {
        RoleEntity rol = new RoleEntity();
        rol.setName(ERole.USER);
        Set<RoleEntity> roles =new HashSet<>();
        roles.add(rol);
        UserEntity userEntity = UserEntity.builder()
                .email(createUserDTO.email())
                .username(createUserDTO.username())
                .password(passwordEncoder.encode(createUserDTO.password()))
                .roles(roles)
                .estado(true)
                .build();

        userRepository.save(userEntity);

        return new CreateUserDTO(userEntity.getUsername(), userEntity.getEmail(), userEntity.getPassword());

    }


    @Override
    @Transactional
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);
        AuthResponse authResponse = new AuthResponse(username, "User loged succesfully", accessToken, true);
        return authResponse;
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException(String.format("Invalid username or password"));
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }
}
