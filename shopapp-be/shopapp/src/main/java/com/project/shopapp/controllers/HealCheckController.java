package com.project.shopapp.controllers;

import com.project.shopapp.models.Category;
import com.project.shopapp.services.category.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/healthcheck")
@AllArgsConstructor
public class HealCheckController {

    private final CategoryService categoryService;

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        // Simple check to see if the application is running
        try{
            List<Category> categories = categoryService.getAll();
            return ResponseEntity.ok("Server is Ok");
        }catch (Exception ex){
            return ResponseEntity.badRequest().body("Server error");
        }
    }


}
