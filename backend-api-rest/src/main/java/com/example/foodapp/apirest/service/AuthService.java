package com.example.foodapp.apirest.service;

import com.example.foodapp.apirest.dto.request.LoginRequest;
import com.example.foodapp.apirest.dto.request.RegisterRequest;
import com.example.foodapp.apirest.dto.response.AuthResponse;
import com.example.foodapp.apirest.entity.RefreshToken;
import com.example.foodapp.apirest.entity.Role;
import com.example.foodapp.apirest.entity.User;
import com.example.foodapp.apirest.exception.ResourceNotFoundException;
import com.example.foodapp.apirest.repository.RoleRepository;
import com.example.foodapp.apirest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        String accessToken = jwtService.generateToken(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", request.getUsername()));

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        log.info("User '{}' logged in successfully", user.getUsername());

        return AuthResponse.of(
                accessToken,
                refreshToken.getToken(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getName()
        );
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use: " + request.getEmail());
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_USER"));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .enabled(true)
                .build();

        user = userRepository.save(user);

        // Auto-login after registration
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        String accessToken = jwtService.generateToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        log.info("User '{}' registered and logged in successfully", user.getUsername());

        return AuthResponse.of(
                accessToken,
                refreshToken.getToken(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getName()
        );
    }

    @Transactional
    public AuthResponse refreshToken(String requestRefreshToken) {
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateTokenFromUsername(user.getUsername());
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
                    return AuthResponse.of(
                            accessToken,
                            newRefreshToken.getToken(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getRole().getName()
                    );
                })
                .orElseThrow(() -> new IllegalArgumentException(
                        "Refresh token is not valid: " + requestRefreshToken));
    }
}
