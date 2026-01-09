package com.example.ecommerce.service;

import com.example.ecommerce.dto.CategoryRequest;
import com.example.ecommerce.dto.CategoryResponse;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Admin Only
    @Transactional
    public CategoryResponse addCategory(CategoryRequest request, Authentication auth) {
        validateAdmin(auth);

        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category already exists");
        }

        Category category = new Category();
        category.setName(request.getName());

        category = categoryRepository.save(category);

        return new CategoryResponse(category.getId(), category.getName());
    }

    // Admin Only
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request, Authentication auth) {
        validateAdmin(auth);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category.setName(request.getName());
        category = categoryRepository.save(category);

        return new CategoryResponse(category.getId(), category.getName());
    }

    // Admin Only
    @Transactional
    public void deleteCategory(Long id, Authentication auth) {
        validateAdmin(auth);

        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found");
        }

        categoryRepository.deleteById(id);
    }

    // Public
    public List<CategoryResponse> listCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    // Service-level Role Check
    private void validateAdmin(Authentication auth) {
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ROLE_ADMIN.name()))) {
            throw new SecurityException("Admin privileges required");
        }
    }
}
