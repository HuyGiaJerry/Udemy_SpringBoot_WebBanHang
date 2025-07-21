package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.services.category.CategoryService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    // Thêm mới 1 category
    @PostMapping("")
    public ResponseEntity<?> createCategory(
            @Valid
            @RequestBody CategoryDTO categoryDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        Category newCategory = categoryService.create(categoryDTO);
        return ResponseEntity.ok("Insert category " + categoryDTO +" successfully");
    }
    // Hiển thị tất cả
    @GetMapping("") // http://localhost:8088/api/v1/categories?page=2&limit=10
    public ResponseEntity<List<Category>> getAllCategories(
            @PathParam("page") int page,
            @PathParam("limit") int limit) {
        List<Category> categories =  categoryService.getAll();
        return ResponseEntity.ok(categories);
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategories(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO

    ) {
        Category updateCate = categoryService.update(id,
                categoryDTO);
        return ResponseEntity.ok("Update category id: " + id + " new category: " + updateCate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable Long id) {

        return ResponseEntity.ok("Delete category with id: " + id+ " successfully");
    }
}
