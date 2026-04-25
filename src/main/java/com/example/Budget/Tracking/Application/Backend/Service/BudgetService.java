package com.example.Budget.Tracking.Application.Backend.Service;

import com.example.Budget.Tracking.Application.Backend.DTO.BudgetRequest;
import com.example.Budget.Tracking.Application.Backend.DTO.BudgetResponse;
import com.example.Budget.Tracking.Application.Backend.Entity.Budget;
import com.example.Budget.Tracking.Application.Backend.Entity.Category;
import com.example.Budget.Tracking.Application.Backend.Entity.User;
import com.example.Budget.Tracking.Application.Backend.Repository.BudgetRepository;
import com.example.Budget.Tracking.Application.Backend.Repository.CategoryRepository;
import com.example.Budget.Tracking.Application.Backend.Repository.TransactionRepository;
import com.example.Budget.Tracking.Application.Backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public BudgetResponse createBudget(BudgetRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Prevent duplicate budgets for the same category in the same month
        Optional<Budget> existing = budgetRepository.findByUser_UsernameAndCategory_CategoryIdAndPeriod(username, category.getCategoryId(), request.getPeriod());
        if (existing.isPresent()) {
            throw new RuntimeException("A budget for this category already exists for " + request.getPeriod());
        }

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(category);
        budget.setAmount(request.getAmount());
        budget.setPeriod(request.getPeriod());

        return mapToResponse(budgetRepository.save(budget), username);
    }

    public BudgetResponse updateBudget(Long id, BudgetRequest request, String username) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }

        budget.setAmount(request.getAmount());
        return mapToResponse(budgetRepository.save(budget), username);
    }

    public void deleteBudget(Long id, String username) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }
        budgetRepository.delete(budget);
    }

    // This method fetches the budgets and calculates the progress dynamically
    public List<BudgetResponse> getBudgetsForPeriod(String username, String period) {
        List<Budget> budgets = budgetRepository.findByUser_UsernameAndPeriod(username, period);
        return budgets.stream().map(budget -> mapToResponse(budget, username)).collect(Collectors.toList());
    }

    private BudgetResponse mapToResponse(Budget budget, String username) {
        // Parse the "YYYY-MM" string to get the first and last day of that month
        YearMonth yearMonth = YearMonth.parse(budget.getPeriod());
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // Query the DB to sum up all expenses for this category in this month
        BigDecimal spent = transactionRepository.getTotalSpentByCategoryAndDateRange(
                username, budget.getCategory().getCategoryId(), startDate, endDate
        );

        return BudgetResponse.builder()
                .budgetId(budget.getBudgetId())
                .categoryId(budget.getCategory().getCategoryId())
                .categoryName(budget.getCategory().getName())
                .amount(budget.getAmount())
                .period(budget.getPeriod())
                .spentSoFar(spent)
                .isExceeded(spent.compareTo(budget.getAmount()) > 0)
                .build();
    }
}