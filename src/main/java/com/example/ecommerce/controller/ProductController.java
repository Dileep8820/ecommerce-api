package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Public endpoints
    @GetMapping
    public ResponseEntity<List<ProductResponse>> listAllProducts() {
        return ResponseEntity.ok(productService.listAllProducts());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    // Admin-only endpoints
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(
            @Validated @RequestBody ProductRequest request,
            Authentication auth) {
        return new ResponseEntity<>(productService.addProduct(request, auth), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Validated @RequestBody ProductRequest request,
            Authentication auth) {
        return ResponseEntity.ok(productService.updateProduct(id, request, auth));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            Authentication auth) {
        productService.deleteProduct(id, auth);
        return ResponseEntity.noContent().build();
    }
}
