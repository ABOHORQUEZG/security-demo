package com.example.foodapp.client.service;

import com.example.foodapp.client.dto.CategoryRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiCategoryService {

    private static final Logger log = LoggerFactory.getLogger(ApiCategoryService.class);
    private final WebClient webClient;

    public List<Map<String, Object>> getAllCategories(String token) {
        log.info("Fetching all categories");
        return webClient.get()
                .uri("/api/categories")
                .headers(h -> addAuthHeader(h, token))
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .map(list -> (List<Map<String, Object>>) (List<?>) list)
                .block();
    }

    public Map<String, Object> getCategoryById(Long id, String token) {
        log.info("Fetching category by id: {}", id);
        return webClient.get()
                .uri("/api/categories/{id}", id)
                .headers(h -> addAuthHeader(h, token))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> createCategory(CategoryRequest request, String token) {
        log.info("Creating category: {}", request.getName());
        return webClient.post()
                .uri("/api/categories")
                .headers(h -> addAuthHeader(h, token))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> updateCategory(Long id, CategoryRequest request, String token) {
        log.info("Updating category id: {}", id);
        return webClient.put()
                .uri("/api/categories/{id}", id)
                .headers(h -> addAuthHeader(h, token))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> deleteCategory(Long id, String token) {
        log.info("Deleting category id: {}", id);
        return webClient.delete()
                .uri("/api/categories/{id}", id)
                .headers(h -> addAuthHeader(h, token))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    private void addAuthHeader(org.springframework.http.HttpHeaders headers, String token) {
        if (token != null && !token.isEmpty()) {
            headers.setBearerAuth(token);
        }
    }
}
