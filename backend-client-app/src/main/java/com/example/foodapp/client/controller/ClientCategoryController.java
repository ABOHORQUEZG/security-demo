package com.example.foodapp.client.controller;

import com.example.foodapp.client.dto.CategoryRequest;
import com.example.foodapp.client.service.ApiCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class ClientCategoryController {

    private final ApiCategoryService apiCategoryService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllCategories(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        return ResponseEntity.ok(apiCategoryService.getAllCategories(extractToken(authHeader)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCategoryById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        return ResponseEntity.ok(apiCategoryService.getCategoryById(id, extractToken(authHeader)));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(
            @RequestBody CategoryRequest request,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiCategoryService.createCategory(request, extractToken(authHeader)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequest request,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(apiCategoryService.updateCategory(id, request, extractToken(authHeader)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(apiCategoryService.deleteCategory(id, extractToken(authHeader)));
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
