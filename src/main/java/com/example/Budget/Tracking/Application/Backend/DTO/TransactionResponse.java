package com.example.Budget.Tracking.Application.Backend.DTO;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionResponse {
    private Long transactionId;
    private String title;
    private BigDecimal amount;
    private String type;
    private LocalDate transactionDate;
    private String note;

    //send back the category details so the frontend can display the name
    private Long categoryId;
    private String categoryName;
}