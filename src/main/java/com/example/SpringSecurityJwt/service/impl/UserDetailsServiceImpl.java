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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private UserRepository userEntityRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
                .collect(Collectors.toSet());

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEstado(),
                true,
                true,
                true,
                authorities);
    }
}
