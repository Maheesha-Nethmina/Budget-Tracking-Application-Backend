package com.example.Budget.Tracking.Application.Backend.DTO;

import com.example.Budget.Tracking.Application.Backend.Entity.Transaction;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardResponse {
    // Top Summary Cards
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal currentBalance;

    // Recent Transactions List
    private List<Transaction> recentTransactions;

    // Chart Data
    private List<Map<String, Object>> expenseByCategoryData; // For Pie Chart
    private List<Map<String, Object>> monthlyData;           // For Bar Chart
}