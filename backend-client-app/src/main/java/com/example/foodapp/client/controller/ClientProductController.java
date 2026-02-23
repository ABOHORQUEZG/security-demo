package com.example.foodapp.client.controller;

import com.example.foodapp.client.dto.ProductRequest;
import com.example.foodapp.client.service.ApiProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ClientProductController {

    private final ApiProductService apiProductService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        return ResponseEntity.ok(
                apiProductService.getAllProducts(page, size, sortBy, sortDir, extractToken(authHeader)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        return ResponseEntity.ok(apiProductService.getProductById(id, extractToken(authHeader)));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        return ResponseEntity.ok(
                apiProductService.getProductsByCategory(categoryId, page, size, extractToken(authHeader)));
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        return ResponseEntity.ok(
                apiProductService.searchProducts(keyword, page, size, extractToken(authHeader)));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(
            @RequestBody ProductRequest request,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiProductService.createProduct(request, extractToken(authHeader)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(apiProductService.updateProduct(id, request, extractToken(authHeader)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(apiProductService.deleteProduct(id, extractToken(authHeader)));
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
