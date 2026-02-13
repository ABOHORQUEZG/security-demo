package com.example.security_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";

    private final AppSecurityProperties appSecurityProperties;

    public SecurityConfig(AppSecurityProperties appSecurityProperties) {
        this.appSecurityProperties = appSecurityProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Usuario ADMIN
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode(appSecurityProperties.getAdminPassword()))
                .roles(ROLE_ADMIN)
                .build();

        // Usuario USER
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode(appSecurityProperties.getUserPassword()))
                .roles(ROLE_USER)
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public", "/", "/login", "/css/**", "/js/**").permitAll()
                .requestMatchers("/access-denied").permitAll()
                .requestMatchers("/admin/**").hasRole(ROLE_ADMIN)
                .requestMatchers("/user/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/user", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/public")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/access-denied")
            );

        return http.build();
    }
}