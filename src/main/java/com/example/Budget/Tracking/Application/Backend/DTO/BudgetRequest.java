package com.example.Budget.Tracking.Application.Backend.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BudgetRequest {
    private Long categoryId;
    private BigDecimal amount;
    private String period;
}