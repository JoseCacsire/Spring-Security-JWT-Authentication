package com.example.SpringSecurityJwt.security.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.SpringSecurityJwt.service.impl.UserDetailsServiceImpl;
import com.example.SpringSecurityJwt.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtTokenValidator extends OncePerRequestFilter {

    private UserDetailsServiceImpl userDetailService;//vamos a necesitar consultar el usuario en la BD

    private JwtUtils jwtUtils;


    public JwtTokenValidator(JwtUtils jwtUtils,UserDetailsServiceImpl userDetailService) {
        this.jwtUtils = jwtUtils;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken != null) {
            jwtToken = jwtToken.substring(7);

            DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

            String username = jwtUtils.extractUsername(decodedJWT);
            log.info("PEPE"+username);
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            log.info("Rol"+userDetails.getAuthorities().toString());

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, null,userDetails.getAuthorities());
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);

        }
        filterChain.doFilter(request, response);
    }
}