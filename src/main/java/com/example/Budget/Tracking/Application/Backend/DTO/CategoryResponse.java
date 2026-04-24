package com.example.Budget.Tracking.Application.Backend.DTO;

import com.example.Budget.Tracking.Application.Backend.Entity.CategoryType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
    private Long categoryId;
    private String name;
    private CategoryType type;
}