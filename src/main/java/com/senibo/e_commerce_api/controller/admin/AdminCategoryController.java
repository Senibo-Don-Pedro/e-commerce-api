package com.senibo.e_commerce_api.controller.admin;

import com.senibo.e_commerce_api.dto.ApiResponseDto;
import com.senibo.e_commerce_api.dto.category.CategoryRequest;
import com.senibo.e_commerce_api.dto.category.CategoryResponse;
import com.senibo.e_commerce_api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/categories")
@PreAuthorize("hasAuthority('ADMIN')")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {

        log.info("Creating category with name: {}", request.name());

        CategoryResponse category = categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(ApiResponseDto.success("Category created successfully",
                                                          category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CategoryResponse>> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequest request) {

        log.info("Updating category with id: {}", id);

        CategoryResponse category = categoryService.updateCategory(id, request);

        return ResponseEntity.ok(ApiResponseDto.success("Category updated successfully", category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteCategory(@PathVariable UUID id) {
        log.info("Deleting category with id: {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponseDto.success("Category deleted successfully", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<CategoryResponse>>> getAllCategories() {
        log.info("Fetching all categories (including inactive)");
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponseDto.success("All categories retrieved successfully", categories));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponseDto<Void>> toggleStatus(@PathVariable UUID id) {
        log.info("Toggling status for category with id: {}", id);
        categoryService.toggleCategoryStatus(id);
        return ResponseEntity.ok(ApiResponseDto.success("Category status updated successfully", null));
    }

}
