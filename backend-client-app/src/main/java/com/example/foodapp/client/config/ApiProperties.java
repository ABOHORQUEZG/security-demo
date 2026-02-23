package com.example.foodapp.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.api")
@Getter
@Setter
public class ApiProperties {

    private String baseUrl = "http://localhost:8080";
}
