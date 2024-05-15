package com.example.SpringSecurityJwt.security.filters;

import com.example.SpringSecurityJwt.security.jwt.JwtUtils;
import com.example.SpringSecurityJwt.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//CLASE PARA VALIDAR EL TOKEN.YA Q SIN ESTO,CUANDO ENVIES EL TOKEN NO TE DEJARA AUTENTICAR.PRIMERO VALIDAR
//Se va a autenticar por cada endpoint.Siempre enviar el token de acceso para poder acceder a los recursos o endpoints
@Component //es un component por q no necestamos enviar ningun parametro adicional
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;//vamos a validar el token
    @Autowired
    private UserDetailsServiceImpl userDetailsService;//vamos a necesitar consultar el usuario en la BD

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        //extrayendo el token de la peticion
        String tokenHeader = request.getHeader("Authorization");

        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")){
            //quitando el Bearer  paa extraer el token real
            String token = tokenHeader.substring(7);

            if (jwtUtils.isTokenValid(token)){
                //obteniendo el usuario
                String username = jwtUtils.getUsernameFromToken(token);
                //obteniendo los detalles del usuario con los permisos q tiene
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                //autenticandonos
                UsernamePasswordAuthenticationToken authenticationToken =
                        //userDetails.getAuthorities():obteniendo los permisos del usuario
                        new UsernamePasswordAuthenticationToken(username,null,userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }

        //continuar con el filtro de validacion ,sino tiene token deniega el acceso
        filterChain.doFilter(request,response);


    }
}
