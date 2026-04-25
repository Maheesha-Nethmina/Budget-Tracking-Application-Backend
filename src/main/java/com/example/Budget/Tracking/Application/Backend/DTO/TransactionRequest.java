package com.example.Budget.Tracking.Application.Backend.DTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {
    private String title;
    private BigDecimal amount;
    private String type;
    private LocalDate transactionDate;
    private String note;
    private Long categoryId;
}