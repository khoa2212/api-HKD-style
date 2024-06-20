package com.example.gateway.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.gateway.dto.AuthResponse;
import com.example.gateway.exception.ErrorMessage;
import com.example.gateway.utils.JWTProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final JWTProvider jwtProvider;
    private final WebClient webClient;

    public AuthFilter(JWTProvider jwtProvider, WebClient webClient) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
        this.webClient = webClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        Logger logger = LoggerFactory.getLogger(AuthFilter.class);
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
               return chain.filter(exchange);
           }
           catch (TokenExpiredException e) {
               ServerHttpRequest request = exchange.getRequest();
               MultiValueMap<String, HttpCookie> cookies = request.getCookies();
               HttpCookie cookie = cookies.getFirst("refreshToken");
               logger.info("starting sending request");
               return webClient.get()
                       .uri("/api/auth/token")
                       .exchangeToMono(response -> {
                           if (response.statusCode().isError()) {
//                               return response.bodyToMono(ErrorMessage.class)
//                                       .flatMap(err -> {
//                                           logger.info("received response");
//                                           logger.info(err.getMessage());
//                                           ServerHttpResponse res = exchange.getResponse();
//                                           DataBuffer buffer = res.bufferFactory().wrap(err.getMessage().getBytes(StandardCharsets.UTF_8));
//                                           res.setStatusCode(HttpStatus.UNAUTHORIZED);
//                                           logger.info(String.valueOf(res.getStatusCode().value()));
//                                           res.writeWith(Mono.just(buffer));
//                                           return res.setComplete();
//                                       });
                               logger.info("received response");
                               ServerHttpResponse res = exchange.getResponse();
                               DataBuffer buffer = res.bufferFactory().wrap("failed to refresh token".getBytes(StandardCharsets.UTF_8));
                               res.setStatusCode(HttpStatus.UNAUTHORIZED);
                               return res.writeWith(Mono.just(buffer));

                           }
                           return response.bodyToMono(AuthResponse.class)
                                   .flatMap(body -> chain.filter(setAuthHeaderWithToken(exchange, body.getAccessToken())));
                       });
//                       .onStatus(HttpStatusCode::isError,
//                               response -> response
//                                       .bodyToMono(ErrorMessage.class)
//                                       .flatMap(body -> Mono.error(() -> new RuntimeException(body.getMessage()))))
//                       .bodyToMono(AuthResponse.class)
//                       .flatMap(response -> {
//                           String accessToken = response.getAccessToken();
//                           return chain.filter(setAuthHeaderWithToken(exchange, accessToken));
//                       });
           }
           catch (JWTVerificationException e) {
               String errorMessage = "Invalid token";
               ServerHttpResponse response = exchange.getResponse();
               DataBuffer buffer = response.bufferFactory().wrap(errorMessage.getBytes(StandardCharsets.UTF_8));
               response.setStatusCode(HttpStatus.UNAUTHORIZED);
               return response.writeWith(Mono.just(buffer));
           }
        });
    }

    private ServerWebExchange setAuthHeaderWithToken(ServerWebExchange exchange, String token) {
        return exchange.mutate()
                .request(req -> req.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .build();
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
