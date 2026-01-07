package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.responses.CategoryResponse;
import com.project.shopapp.services.category.CategoryService;
import com.project.shopapp.utils.LocalizationUtil;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Validated
// Dependency Injection
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final CategoryService categoryService;
    // Inject Component for Response and Language
    private final LocalizationUtil localizationUtil;

    // Thêm mới 1 category
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid
            @RequestBody CategoryDTO categoryDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(CategoryResponse
                    .builder()
                    .message(localizationUtil.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_FAILED, errorMessages.toString()))
                    .build()
            );
        }
        Category newCategory = categoryService.create(categoryDTO);
        return ResponseEntity.ok(CategoryResponse
                .builder()
                .id(newCategory.getId())
                .name(newCategory.getName())
                .message(localizationUtil.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESS))
                .build()
        );
    }

    // Hiển thị tất cả
    // http://localhost:8088/api/v1/categories?page=2&limit=10
    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @PathParam("page") int page,
            @PathParam("limit") int limit) {
        List<Category> categories = categoryService.getAll();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategories(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO
    ) {
        Category updateCate = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(CategoryResponse
                .builder()
                .message(localizationUtil.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESS))
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponse> deleteCategories(@PathVariable Long id) {

        return ResponseEntity.ok( CategoryResponse
                .builder()
                .message(localizationUtil.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESS, id.toString()))
                .build()
        );
    }
}
