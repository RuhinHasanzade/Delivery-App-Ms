package com.example.menuservice_ms.service;

import com.example.menuservice_ms.dto.CategoryRequest;
import com.example.menuservice_ms.exception.BadRequestException;
import com.example.menuservice_ms.exception.NotFoundException;
import com.example.menuservice_ms.model.Category;
import com.example.menuservice_ms.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() {
        List<Category> result = categoryRepository.findAll();
        result.sort(Comparator.comparingLong(Category::getId));
        return result;
    }

    public boolean existsById(Long id) {
        return id != null && categoryRepository.existsById(id);
    }

    public Category getById(Long id) {
        if (id == null) {
            throw new NotFoundException("Category not found: " + id);
        }

        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
    }

    public Category create(CategoryRequest request) {
        validateRequest(request);

        Category category = Category.builder()
                .name(request.getName().trim())
                .build();

        return categoryRepository.save(category);
    }

    public Category update(Long id, CategoryRequest request) {
        if (id == null) {
            throw new NotFoundException("Category not found: " + id);
        }

        validateRequest(request);

        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));

        existing.setName(request.getName().trim());

        return categoryRepository.save(existing);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new NotFoundException("Category not found: " + id);
        }

        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));

        categoryRepository.delete(existing);
    }

    private void validateRequest(CategoryRequest request) {
        if (request == null || request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Category name is required", List.of("name"));
        }
    }
}