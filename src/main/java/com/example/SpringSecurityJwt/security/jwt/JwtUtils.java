package com.example.SpringSecurityJwt.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

//esta clase nos va a proveer los metodos necesarios para
//trabajar con el token
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret.key}")//asignandole valor
    private String secretKey;//nos ayuda a firmar el token q generamos.
    @Value("${jwt.time.expiration}")
    private String timeExpiration;//tiempo de validez del token

    //Un metodo para generar un token de acceso
    public String generateAccesToken(String username){
        return Jwts.builder()
                .setSubject(username)//mandando el usuario q va a generar el token
                .setIssuedAt(new Date(System.currentTimeMillis()))//fecha de creacion del token
                .setExpiration(new Date(System.currentTimeMillis()+Long.parseLong(timeExpiration)))//cuando va a expirar
                //obteniendo la firma del token y luego encriptandola
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)//poner la firma del metodo y encriptarla otra vezx|
                .compact();
    }

    //Validar el token de acceso
    public boolean isTokenValid(String token){
        try {
            Jwts.parserBuilder() //leer el token
                    .setSigningKey(getSignatureKey()) //q este firmado sino token invalido
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        }catch (Exception e){
            log.error("Token invalido,error: ".concat(e.getMessage()));
            return false;
        }
    }

    //obteniendo el username del token
    public String getUsernameFromToken(String token){
        return getClaim(token,Claims::getSubject);
    }

    //Para obtener un solo claim
    public  <T> T getClaim(String token, Function<Claims,T> claimsTFunction){
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    //obtener todos los claims del token
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder() //leer el token
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //obtener firma del token
    public Key getSignatureKey(){
        //decodificando secretKey
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
