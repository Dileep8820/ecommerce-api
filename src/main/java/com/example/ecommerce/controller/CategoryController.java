package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CategoryRequest;
import com.example.ecommerce.dto.CategoryResponse;
import com.example.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Public endpoint
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listCategories() {
        return ResponseEntity.ok(categoryService.listCategories());
    }

    // Admin-only endpoints
    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(
            @Validated @RequestBody CategoryRequest request,
            Authentication auth) {

        CategoryResponse response = categoryService.addCategory(request, auth);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Validated @RequestBody CategoryRequest request,
            Authentication auth) {

        CategoryResponse response = categoryService.updateCategory(id, request, auth);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id,
            Authentication auth) {

        categoryService.deleteCategory(id, auth);
        return ResponseEntity.noContent().build();
    }
}
