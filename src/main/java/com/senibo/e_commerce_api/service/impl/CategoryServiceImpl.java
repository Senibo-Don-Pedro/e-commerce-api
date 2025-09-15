package com.senibo.e_commerce_api.service.impl;

import com.senibo.e_commerce_api.dto.category.CategoryRequest;
import com.senibo.e_commerce_api.dto.category.CategoryResponse;
import com.senibo.e_commerce_api.exception.category.CategoryDeletionException;
import com.senibo.e_commerce_api.exception.category.CategoryNotFoundException;
import com.senibo.e_commerce_api.exception.product.InvalidProductOperationException;
import com.senibo.e_commerce_api.model.category.Category;
import com.senibo.e_commerce_api.repository.CategoryRepository;
import com.senibo.e_commerce_api.repository.ProductRepository;
import com.senibo.e_commerce_api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        log.info("Creating category with name: {}", request.name());

        // Generate slug from name
        String slug = generateSlug(request.name());
        slug = ensureUniqueSlug(slug, null);

        Category category = Category.builder()
                                    .name(request.name())
                                    .slug(slug)
                                    .description(request.description())
                                    .sortOrder(request.sortOrder() != null ? request.sortOrder() : 0)
                                    .isActive(true) // Always true for new categories
                                    .build();

        Category saved = categoryRepository.save(category);
        log.info("Category created successfully with id: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        log.info("Updating category with id: {}", id);

        Category category = findCategoryEntityById(id);

        // Check if name changed and update slug if needed
        if (!category.getName().equals(request.name())) {
            String newSlug = generateSlug(request.name());
            newSlug = ensureUniqueSlug(newSlug, id);
            category.setSlug(newSlug);
            log.info("Updated slug for category {} from {} to {}", id, category.getSlug(), newSlug);
        }

        // Update fields
        category.setName(request.name());
        category.setDescription(request.description());
        category.setSortOrder(request.sortOrder() != null ? request.sortOrder() : category.getSortOrder());

        // Only update isActive if provided (for updates only)
        if (request.isActive() != null) {
            category.setIsActive(request.isActive());
        }

        Category updated = categoryRepository.save(category);
        log.info("Category updated successfully: {}", id);

        return mapToResponse(updated);
    }

    @Override
    public void deleteCategory(UUID id) {
        log.info("Attempting to delete category with id: {}", id);

        Category category = findCategoryEntityById(id);

        // Business rule: Can't delete category with active products
        if (productRepository.existsByCategoryAndIsActiveTrue(category)) {
            throw new CategoryDeletionException(
                    "Cannot delete category '" + category.getName() + "' because it contains active products");
        }

        categoryRepository.delete(category);
        log.info("Category deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryByIdentifier(String identifier) {
        log.info("Fetching category with identifier: {}", identifier);

        Category category;

        // Check if it's a UUID format
        if (isValidUUID(identifier)) {
            category = categoryRepository.findById(UUID.fromString(identifier))
                                         .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + identifier));
        } else {
            // Treat as slug - only return active categories for public access
            category = categoryRepository.findBySlugAndIsActiveTrue(identifier)
                                         .orElseThrow(() -> new CategoryNotFoundException("Active category not found with slug: " + identifier));
        }

        return mapToResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllActiveCategories() {
        log.info("Fetching all active categories");

        List<Category> categories = categoryRepository.findByIsActiveTrueOrderBySortOrderAsc();
        return categories.stream()
                         .map(this::mapToResponse)
                         .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        log.info("Fetching all categories (including inactive)");

        List<Category> categories = categoryRepository.findAllByOrderBySortOrderAsc();
        return categories.stream()
                         .map(this::mapToResponse)
                         .toList();
    }

    @Override
    public void toggleCategoryStatus(UUID id) {
        log.info("Toggling status for category: {}", id);

        Category category = findCategoryEntityById(id);

        // If deactivating, check for active products
        if (category.getIsActive() && productRepository.existsByCategoryAndIsActiveTrue(category)) {
            throw new InvalidProductOperationException(
                    "Cannot deactivate category '" + category.getName() + "' because it contains active products");
        }

        category.setIsActive(!category.getIsActive());
        categoryRepository.save(category);

        log.info("Category {} status changed to: {}", id, category.getIsActive());
    }

    @Override
    @Transactional(readOnly = true)
    public Category findCategoryEntityById(UUID id) {
        return categoryRepository.findById(id)
                                 .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    // Helper methods
    private String generateSlug(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty for slug generation");
        }

        return name.toLowerCase()
                   .replaceAll("[^a-z0-9\\s-]", "") // Remove special characters
                   .replaceAll("\\s+", "-")          // Replace spaces with hyphens
                   .replaceAll("-+", "-")            // Replace multiple hyphens with single
                   .replaceAll("^-|-$", "");         // Trim hyphens from start/end
    }

    private String ensureUniqueSlug(String baseSlug, UUID excludeId) {
        String slug = baseSlug;
        int counter = 1;

        while (slugExists(slug, excludeId)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    private boolean slugExists(String slug, UUID excludeId) {
        if (excludeId == null) {
            return categoryRepository.existsBySlug(slug);
        }
        return categoryRepository.existsBySlugAndIdNot(slug, excludeId);
    }

    private boolean isValidUUID(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private CategoryResponse mapToResponse(Category category) {
        Integer productCount = categoryRepository.countActiveProductsByCategory(category);

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getIsActive(),
                category.getSortOrder(),
                productCount,
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
