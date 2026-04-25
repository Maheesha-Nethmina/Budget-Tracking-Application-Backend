package com.example.Budget.Tracking.Application.Backend.Service;

import com.example.Budget.Tracking.Application.Backend.DTO.DashboardResponse;
import com.example.Budget.Tracking.Application.Backend.Entity.Transaction;
import com.example.Budget.Tracking.Application.Backend.Repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardResponse getDashboardData(String username) {
        // 1. Fetch all transactions for this user (they are already sorted newest first by the repo)
        List<Transaction> allTransactions = transactionRepository.findByUser_UsernameOrderByTransactionDateDesc(username);

        // 2. Calculate Top-Level Summaries
        BigDecimal totalIncome = allTransactions.stream()
                .filter(t -> "INCOME".equals(t.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = allTransactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpenses);

        // 3. Get Recent Transactions (Top 5)
        List<Transaction> recent = allTransactions.stream()
                .limit(5)
                .collect(Collectors.toList());

        // 4. Group Expenses by Category (For the Pie Chart)
        Map<String, BigDecimal> categoryTotals = allTransactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        List<Map<String, Object>> pieChartData = categoryTotals.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", entry.getKey());
                    map.put("value", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        // 5. Group Income & Expenses by Month (For the Bar Chart)
        // Format dates as "YYYY-MM"
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        Map<String, Map<String, BigDecimal>> monthlyGrouped = new TreeMap<>(); // TreeMap keeps months sorted

        for (Transaction t : allTransactions) {
            String monthKey = t.getTransactionDate().format(monthFormatter);
            monthlyGrouped.putIfAbsent(monthKey, new HashMap<>(Map.of("income", BigDecimal.ZERO, "expense", BigDecimal.ZERO)));

            if ("INCOME".equals(t.getType())) {
                BigDecimal currentInc = monthlyGrouped.get(monthKey).get("income");
                monthlyGrouped.get(monthKey).put("income", currentInc.add(t.getAmount()));
            } else {
                BigDecimal currentExp = monthlyGrouped.get(monthKey).get("expense");
                monthlyGrouped.get(monthKey).put("expense", currentExp.add(t.getAmount()));
            }
        }

        List<Map<String, Object>> barChartData = monthlyGrouped.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("month", entry.getKey());
                    map.put("income", entry.getValue().get("income"));
                    map.put("expense", entry.getValue().get("expense"));
                    return map;
                })
                .collect(Collectors.toList());

        // 6. Build and Return Response
        return DashboardResponse.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .currentBalance(balance)
                .recentTransactions(recent)
                .expenseByCategoryData(pieChartData)
                .monthlyData(barChartData)
                .build();
    }
}