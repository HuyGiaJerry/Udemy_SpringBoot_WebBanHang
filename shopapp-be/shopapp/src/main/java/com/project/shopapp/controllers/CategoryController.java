package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@Validated
@RequestMapping("api/v1/categories")
public class CategoryController {
    // Hiển thị tất cả
    @GetMapping("") // http://localhost:8088/api/v1/categories?page=2&limit=10
    public ResponseEntity<String> getAllCategories(@PathParam("page") int page, @PathParam("limit") int limit) {
        return ResponseEntity.ok("All categories in page " + page + " limit " + limit);
    }

    @PostMapping("")
    public ResponseEntity<?> insertCategories(
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
        return ResponseEntity.ok("Insert category " + categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategories(@PathVariable Long id) {
        return ResponseEntity.ok("Update category id: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable Long id) {
        return ResponseEntity.ok("Delete category id: " + id);
    }
}
