package com.example.Budget.Tracking.Application.Backend.DTO;

import com.example.Budget.Tracking.Application.Backend.Entity.CategoryType;
import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private CategoryType type;
}