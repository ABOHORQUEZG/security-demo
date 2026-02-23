package com.example.foodapp.client.controller;

import com.example.foodapp.client.dto.AuthResponse;
import com.example.foodapp.client.dto.LoginRequest;
import com.example.foodapp.client.dto.RefreshTokenRequest;
import com.example.foodapp.client.dto.RegisterRequest;
import com.example.foodapp.client.service.ApiAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ClientAuthController {

    private final ApiAuthService apiAuthService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(apiAuthService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(apiAuthService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(apiAuthService.refreshToken(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(apiAuthService.forgotPassword(email));
    }
}
