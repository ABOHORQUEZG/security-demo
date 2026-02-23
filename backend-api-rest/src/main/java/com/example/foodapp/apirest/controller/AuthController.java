package com.example.foodapp.apirest.controller;

import com.example.foodapp.apirest.dto.request.LoginRequest;
import com.example.foodapp.apirest.dto.request.RefreshTokenRequest;
import com.example.foodapp.apirest.dto.request.RegisterRequest;
import com.example.foodapp.apirest.dto.response.AuthResponse;
import com.example.foodapp.apirest.dto.response.MessageResponse;
import com.example.foodapp.apirest.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Auth management endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and return JWT tokens")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Register a new user and return JWT tokens")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Refresh an expired access token")
    public ResponseEntity<AuthResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Request password recovery email (mock)")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(MessageResponse.of(
                "If the email '" + email + "' is registered, a recovery link has been sent."));
    }
}
