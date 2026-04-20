package com.example.menuservice_ms.service;

import com.example.menuservice_ms.dto.FoodAvailabilityRequest;
import com.example.menuservice_ms.dto.FoodRequest;
import com.example.menuservice_ms.exception.BadRequestException;
import com.example.menuservice_ms.exception.NotFoundException;
import com.example.menuservice_ms.model.Category;
import com.example.menuservice_ms.model.Food;
import com.example.menuservice_ms.repository.CategoryRepository;
import com.example.menuservice_ms.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;

    public FoodService(FoodRepository foodRepository,
                       CategoryRepository categoryRepository) {
        this.foodRepository = foodRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Food> getAll() {
        List<Food> result = foodRepository.findAll();
        result.sort(Comparator.comparingLong(Food::getId));
        return result;
    }

    public Food getById(Long id) {
        if (id == null) {
            throw new NotFoundException("Food not found: " + id);
        }

        return foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food not found: " + id));
    }

    public Food create(FoodRequest request) {
        validateCreateRequest(request);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BadRequestException("`categoryId` does not exist", List.of("categoryId")));

        Food food = Food.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(category)
                .available(Boolean.TRUE.equals(request.getAvailable()))
                .build();

        return foodRepository.save(food);
    }

    public Food update(Long id, FoodRequest request) {
        validateUpdateRequest(request);

        Food existing = foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food not found: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BadRequestException("`categoryId` does not exist", List.of("categoryId")));

        existing.setName(request.getName().trim());
        existing.setDescription(request.getDescription());
        existing.setCategory(category);

        if (request.getAvailable() != null) {
            existing.setAvailable(Boolean.TRUE.equals(request.getAvailable()));
        }

        return foodRepository.save(existing);
    }

    public Food patchAvailability(Long id, FoodAvailabilityRequest request) {
        if (request == null || request.getAvailable() == null) {
            throw new BadRequestException("`available` is required", List.of("available"));
        }

        Food existing = foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food not found: " + id));

        existing.setAvailable(Boolean.TRUE.equals(request.getAvailable()));

        return foodRepository.save(existing);
    }

    public void delete(Long id) {
        Food existing = foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food not found: " + id));

        foodRepository.delete(existing);
    }

    private void validateCreateRequest(FoodRequest request) {
        validateUpdateRequest(request);
        if (request.getAvailable() == null) {
            throw new BadRequestException("`available` is required", List.of("available"));
        }
    }

    private void validateUpdateRequest(FoodRequest request) {
        if (request == null) {
            throw new BadRequestException("Request body is required", List.of("body"));
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("`name` is required", List.of("name"));
        }
        if (request.getCategoryId() == null) {
            throw new BadRequestException("`categoryId` is required", List.of("categoryId"));
        }
    }
}