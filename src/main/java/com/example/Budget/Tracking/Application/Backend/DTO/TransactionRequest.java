package com.example.Budget.Tracking.Application.Backend.DTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {
    private String title;
    private BigDecimal amount;
    private String type; // INCOME or EXPENSE
    private LocalDate transactionDate;
    private String note; // Optional note as required by assignment
    private Long categoryId;
}