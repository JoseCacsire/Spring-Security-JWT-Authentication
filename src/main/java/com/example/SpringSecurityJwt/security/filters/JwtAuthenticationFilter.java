package com.example.SpringSecurityJwt.security.filters;

import com.example.SpringSecurityJwt.models.UserEntity;
import com.example.SpringSecurityJwt.security.jwt.JwtUtils;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//para inverstigar en chatgtp "¿por q no tiene una anotacion @Bean?"
//Este UsernamePasswordAuthenticationFilter es quien nos va a ayudar a autenticarnos en nuestra aplicacion
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtUtils jwtUtils;

    //Como no se puede usar un @Autowired.Tenemos  pasarlo por el constructor
    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    //INTENTAR AUTENTICARSE
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserEntity userEntity = null;
        String username = "";
        String password = "";
        try {
            //sino hay ningun error entonces nos vamos a autenticar
            //tomas los parametros username y password que se mandaran en el postman y los vas a mapear en un userEntity
            //q serian el usuario y el password.Trabando con jackson q sirve para trabajar con json y mapearlos a objetos java
            userEntity = new ObjectMapper().readValue(request.getInputStream(),UserEntity.class);
            username = userEntity.getUsername();
            password = userEntity.getPassword();
        }catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //con esto nos vamos a autenticar en la aplicacion
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    //Si la autenticacion es correcta vamos a generar el token
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //obteniendo el objeto q contiene todos los detalles del usuario
        User user = (User) authResult.getPrincipal();
        //generando el token del acceso para dar autorizacion de acceso a los otros endpoint
        String token = jwtUtils.generateAccesToken(user.getUsername());
        //enviando el token en la respuesta
        response.addHeader("Authorization",token);
        Map<String,Object> httpResponse = new HashMap<>();
        httpResponse.put("tokens",token);
        httpResponse.put("Message","Autenticacion correcta");
        httpResponse.put("Username",user.getUsername());

        //escribiendo el Map como json en la respuesta.Usando la libreria JACKSON
        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //Asegurando q todo se escriba correctamente
        response.getWriter().flush();


        super.successfulAuthentication(request, response, chain, authResult);
    }
}
