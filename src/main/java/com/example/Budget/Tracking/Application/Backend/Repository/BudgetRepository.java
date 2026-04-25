package com.example.Budget.Tracking.Application.Backend.Repository;

import com.example.Budget.Tracking.Application.Backend.Entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // Find all budgets for a user in a specific month
    List<Budget> findByUser_UsernameAndPeriod(String username, String period);

    // Check if a specific category already has a budget for this month
    Optional<Budget> findByUser_UsernameAndCategory_CategoryIdAndPeriod(String username, Long categoryId, String period);
}