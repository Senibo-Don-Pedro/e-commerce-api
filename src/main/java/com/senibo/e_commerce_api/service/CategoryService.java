package com.senibo.e_commerce_api.service;

import com.senibo.e_commerce_api.dto.category.CategoryRequest;
import com.senibo.e_commerce_api.dto.category.CategoryResponse;
import com.senibo.e_commerce_api.model.category.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    // CRUD operations
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(UUID id, CategoryRequest request);
    void deleteCategory(UUID id);
    CategoryResponse getCategoryByIdentifier(String identifier); // ID or slug
    
    // Business queries
    List<CategoryResponse> getAllActiveCategories();
    List<CategoryResponse> getAllCategories(); // Admin only
    
    // Admin operations
    void toggleCategoryStatus(UUID id);
    
    // Internal method for validation
    Category findCategoryEntityById(UUID id); // Used by ProductService
}
