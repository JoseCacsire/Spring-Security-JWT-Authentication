package com.example.SpringSecurityJwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {

    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    public String createToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        String username = authentication.getPrincipal().toString();

        return JWT.create()
                .withIssuer(this.userGenerator)       // Crea un campo 'iss' con el valor de userGenerator
                .withSubject(username)                // Crea un campo 'sub' con el valor del username
                .withIssuedAt(new Date())             // Crea un campo 'iat' con la fecha y hora actuales
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))  // Crea un campo 'exp' con la fecha y hora de expiración (30 minutos después)
                .withJWTId(UUID.randomUUID().toString())  // Crea un campo 'jti' con un UUID aleatorio
                .withNotBefore(new Date(System.currentTimeMillis()))  // Crea un campo 'nbf' con la fecha y hora actuales
                .sign(algorithm); // Firma el token con el algoritmo HMAC256 y la clave privada
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token invalid, not Authorized");
        }
    }

    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }
}