
package com.budgetwise.service;

import com.budgetwise.model.Expense;
import com.budgetwise.model.Budget;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {

    public BigDecimal calculateTotal(List<Expense> expenses) {
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateMonthlySpending(
            List<Expense> expenses, YearMonth month) {
        return expenses.stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(month))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateRemainingBudget(
            Budget budget, List<Expense> expenses) {
        BigDecimal spent = calculateMonthlySpending(
                expenses, budget.getMonth());
        return budget.getMonthlyLimit().subtract(spent);
    }

    public Map<String, BigDecimal> spendingByCategory(
            List<Expense> expenses, YearMonth month) {
        return expenses.stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(month))
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add)));
    }
}
