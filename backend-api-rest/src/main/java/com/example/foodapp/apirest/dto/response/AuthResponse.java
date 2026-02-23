package com.example.foodapp.apirest.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String username;
    private String email;
    private String role;

    @Builder.Default
    private String tokenTypeDefault = "Bearer";

    public static AuthResponse of(String accessToken, String refreshToken,
                                   String username, String email, String role) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .username(username)
                .email(email)
                .role(role)
                .build();
    }
}
