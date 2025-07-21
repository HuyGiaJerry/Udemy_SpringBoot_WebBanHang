package com.project.shopapp.services.category;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.helpers.services.BaseServiceImpl;
import com.project.shopapp.models.Category;
import com.project.shopapp.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService extends BaseServiceImpl<Category, CategoryDTO, Long> {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, CategoryDTO dto) {
        // Tim theo id
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        // Lấy ra category hiện tại
        Category category = optionalCategory.get();
        // Cập nhật
        category.setName(dto.getName());
        // Lưu lại db
        return categoryRepository.save(category);
    }
}
