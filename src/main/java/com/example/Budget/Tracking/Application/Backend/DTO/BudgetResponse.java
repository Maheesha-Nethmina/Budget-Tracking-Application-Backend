package com.example.Budget.Tracking.Application.Backend.DTO;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class BudgetResponse {
    private Long budgetId;
    private Long categoryId;
    private String categoryName;
    private BigDecimal amount;
    private String period;
    private BigDecimal spentSoFar;
    private boolean isExceeded;
}