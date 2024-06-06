package com.example.gateway.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.gateway.utils.JWTProvider;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private JWTProvider jwtProvider;

    public AuthFilter(JWTProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
           String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
           if (authHeader == null || !authHeader.startsWith("Bearer ")) {
               ServerHttpResponse response = exchange.getResponse();
               response.setStatusCode(HttpStatus.UNAUTHORIZED);
               return response.setComplete();
           }

           String token = authHeader.substring("Bearer ".length());
           try {
               jwtProvider.validateToken(token);
           }
           catch (JWTVerificationException e) {
               String errorMessage = "Invalid token";
               ServerHttpResponse response = exchange.getResponse();
               DataBuffer buffer = response.bufferFactory().wrap(errorMessage.getBytes(StandardCharsets.UTF_8));
               response.setStatusCode(HttpStatus.UNAUTHORIZED);
               return response.writeWith(Mono.just(buffer));
           }

           return chain.filter(exchange);
        });
    }

    public static class Config {
        private final String SECRET_KEY;

        public Config(String secretKey) {
            this.SECRET_KEY = secretKey;
        }

        public String getSecretKey() {
            return this.SECRET_KEY;
        }
    }
}
