package com.example.apidemo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScope
@Component
public class JWTService {
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    private final String issuer = "HKD Style";

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

    public String extractEmailFromToken(String token) throws JWTVerificationException {
        Map<String, String> payload = validateToken(token);
        return payload.get("email");
    }

    public UUID extractUserIdFromToken(String token) throws JWTVerificationException {
        Map<String, String> payload = validateToken(token);
        return UUID.fromString(payload.get("sub"));
    }
}
