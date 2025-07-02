package com.project.shopapp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {
    // Hiển thị tất cả
    @GetMapping("")
    public ResponseEntity<String> getAllCategories() {
        return ResponseEntity.ok("All categories");
    }

    @PostMapping("")
    public ResponseEntity<String> insertCategories() {
        return ResponseEntity.ok("Insert categories");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategories(@PathVariable Long id) {
        return ResponseEntity.ok("Update category id: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable Long id) {
        return ResponseEntity.ok("Delete category id: "+id);
    }
}
