package com.example.menuservice_ms.repository;

import com.example.menuservice_ms.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}