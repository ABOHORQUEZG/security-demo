package com.example.foodapp.client.service;

import com.example.foodapp.client.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiProductService {

    private static final Logger log = LoggerFactory.getLogger(ApiProductService.class);
    private final WebClient webClient;

    public Map<String, Object> getAllProducts(int page, int size, String sortBy, String sortDir, String token) {
        log.info("Fetching products - page: {}, size: {}", page, size);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/products")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("sortBy", sortBy)
                        .queryParam("sortDir", sortDir)
                        .build())
                .headers(h -> addAuthHeader(h, token))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> getProductById(Long id, String token) {
        log.info("Fetching product by id: {}", id);
        return webClient.get()
                .uri("/api/products/{id}", id)
                .headers(h -> addAuthHeader(h, token))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> getProductsByCategory(Long categoryId, int page, int size, String token) {
        log.info("Fetching products by category: {}", categoryId);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/products/category/{categoryId}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(categoryId))
                .headers(h -> addAuthHeader(h, token))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> searchProducts(String keyword, int page, int size, String token) {
        log.info("Searching products with keyword: {}", keyword);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/products/search")
                        .queryParam("keyword", keyword)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .headers(h -> addAuthHeader(h, token))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> createProduct(ProductRequest request, String token) {
        log.info("Creating product: {}", request.getName());
        return webClient.post()
                .uri("/api/products")
                .headers(h -> addAuthHeader(h, token))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> updateProduct(Long id, ProductRequest request, String token) {
        log.info("Updating product id: {}", id);
        return webClient.put()
                .uri("/api/products/{id}", id)
                .headers(h -> addAuthHeader(h, token))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> deleteProduct(Long id, String token) {
        log.info("Deleting product id: {}", id);
        return webClient.delete()
                .uri("/api/products/{id}", id)
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
