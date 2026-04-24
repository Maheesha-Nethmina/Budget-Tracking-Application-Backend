package com.example.Budget.Tracking.Application.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    // Standard error format for the frontend
    private String message;
}