package com.example.Budget.Tracking.Application.Backend.Repository;

import com.example.Budget.Tracking.Application.Backend.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Get all transactions for a specific user (ordered newest first)
    List<Transaction> findByUser_UsernameOrderByTransactionDateDesc(String username);

    // Filter by Type (Income/Expense)
    List<Transaction> findByUser_UsernameAndTypeOrderByTransactionDateDesc(String username, String type);

    // Filter by Category
    List<Transaction> findByUser_UsernameAndCategory_CategoryIdOrderByTransactionDateDesc(String username, Long categoryId);

    // Filter by Date Range (For dashboards and reports)
    @Query("SELECT t FROM Transaction t WHERE t.user.username = :username AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<Transaction> findByDateRange(
            @Param("username") String username,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}