package com.example.gateway.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.gateway.utils.JWTProvider;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final JWTProvider jwtProvider;

    public AuthFilter(JWTProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
           ServerHttpRequest request = exchange.getRequest();
           ServerHttpResponse response = exchange.getResponse();

           if (isUnauthenticatedEndpoint(request.getPath().pathWithinApplication().value(), request.getMethod()))
               return chain.filter(exchange);

           String authHeader = request.getHeaders().getFirst("Authorization");
           if (authHeader == null || !authHeader.startsWith("Bearer ")) {
               response.setStatusCode(HttpStatus.UNAUTHORIZED);
               return response.setComplete();
           }

           String accessToken = authHeader.substring("Bearer ".length());
           try {
               jwtProvider.validateToken(accessToken);
               return chain.filter(exchange);
           }
           catch (JWTVerificationException e) {
               String errorMessage = "Invalid token";
               DataBuffer buffer = response.bufferFactory().wrap(errorMessage.getBytes(StandardCharsets.UTF_8));
               response.setStatusCode(HttpStatus.UNAUTHORIZED);
               return response.writeWith(Mono.just(buffer));
           }
        });
    }

    private boolean isUnauthenticatedEndpoint(String endpoint, HttpMethod method) {
        List<String> unauthEndpoints = List.of("/api/users", "/api/users/forgot-password", "/api/users/reset-password");
        return unauthEndpoints.stream().anyMatch(url -> {
            if (endpoint.equals("/api/users"))
                return method == HttpMethod.POST;
            return url.equals(endpoint);
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
