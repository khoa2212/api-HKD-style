package com.example.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.Instant;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@ApplicationScope
@Component
public class JWTService {
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    private final String issuer = "HKD Style";

    /**
     * Generate a JWT token
     @param subject The subject of this token
     @param expiredTime The expired time of this token in seconds
     @param claims The custom claims for this token
     @return A signed token string
    **/
    public String generateToken(String subject, int expiredTime, Map<String, String> claims) throws JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withSubject(subject)
                .withIssuer(issuer)
                .withIssuedAt(Instant.now())
                .withPayload(claims)
                .withExpiresAt(Instant.now().plusSeconds(expiredTime))
                .sign(algorithm);
    }

    public Map<String, String> validateToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT
                .require(algorithm)
                .withIssuer(issuer)
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);

        Map<String, String> claims = new HashMap<>();
        for (String k : decodedJWT.getClaims().keySet()) {
            claims.put(k, decodedJWT.getClaim(k).asString());
        }
        return claims;
    }

}
