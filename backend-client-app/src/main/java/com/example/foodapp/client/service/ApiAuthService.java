package com.example.foodapp.client.service;

import com.example.foodapp.client.dto.AuthResponse;
import com.example.foodapp.client.dto.LoginRequest;
import com.example.foodapp.client.dto.RefreshTokenRequest;
import com.example.foodapp.client.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiAuthService {

    private static final Logger log = LoggerFactory.getLogger(ApiAuthService.class);
    private final WebClient webClient;

    public AuthResponse login(LoginRequest request) {
        log.info("Sending login request for user: {}", request.getUsername());
        return webClient.post()
                .uri("/api/auth/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block();
    }

    public AuthResponse register(RegisterRequest request) {
        log.info("Sending register request for user: {}", request.getUsername());
        return webClient.post()
                .uri("/api/auth/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.info("Sending refresh token request");
        return webClient.post()
                .uri("/api/auth/refresh")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block();
    }

    public Map<String, String> forgotPassword(String email) {
        log.info("Sending forgot password request for email: {}", email);
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/auth/forgot-password")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
