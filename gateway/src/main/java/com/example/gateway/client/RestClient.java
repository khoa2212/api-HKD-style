package com.example.gateway.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestClient {
    @Bean
    public WebClient authClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://auth-service:8081").build();
    }
}
