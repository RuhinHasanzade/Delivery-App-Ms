package com.example.menuservice_ms.repository;

import com.example.menuservice_ms.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}