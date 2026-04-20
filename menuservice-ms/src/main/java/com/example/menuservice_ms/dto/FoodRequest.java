package com.example.menuservice_ms.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FoodRequest {
    private String name;
    private String description;
    private Long categoryId;
    private BigDecimal price;
    private Boolean available;
}