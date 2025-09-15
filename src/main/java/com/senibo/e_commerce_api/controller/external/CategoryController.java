package com.senibo.e_commerce_api.controller.external;

import com.senibo.e_commerce_api.dto.ApiResponseDto;
import com.senibo.e_commerce_api.dto.category.CategoryResponse;
import com.senibo.e_commerce_api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<CategoryResponse>>> getAllActiveCategories() {
        log.info("Fetching all active categories");
        List<CategoryResponse> categories = categoryService.getAllActiveCategories();

        return ResponseEntity.ok(ApiResponseDto.success("Categories retrieved successfully", categories));
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<ApiResponseDto<CategoryResponse>> getCategory(@PathVariable String identifier) {
        log.info("Fetching category with identifier: {}", identifier);
        CategoryResponse category = categoryService.getCategoryByIdentifier(identifier);
        return ResponseEntity.ok(ApiResponseDto.success("Category retrieved successfully", category));
    }
}
