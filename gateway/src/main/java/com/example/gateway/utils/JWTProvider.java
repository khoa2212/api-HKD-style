package com.example.gateway.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTProvider {
    private final String issuer = "HKD Style";
    private final int expireTime = 3 * 60 * 60;
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    public Map<String, String> validateToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        DecodedJWT decodedJWT;
        Map<String, String> claims = new HashMap<>();

        decodedJWT = JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);
        decodedJWT.getClaims().keySet().forEach(k -> {
            claims.put(k, decodedJWT.getClaim(k).asString());
        });

        return claims;
    }

    public String generateToken(Map<String, String> payload) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withIssuer(this.issuer)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(expireTime))
                .sign(algorithm);

    }
}
