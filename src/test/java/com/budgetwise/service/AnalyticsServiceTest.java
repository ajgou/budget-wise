package com.budgetwise.service;

import com.budgetwise.model.Expense;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnalyticsServiceTest {

    private final AnalyticsService service = new AnalyticsService();

    private Expense expense(String amount, LocalDate date, String category) {
        return new Expense(
                1L,
                new BigDecimal(amount),
                category,
                date,
                "Test expense"
        );
    }

    @Test
    void calculateTotalSumsAllExpenseAmounts() {
        List<Expense> expenses = List.of(
                expense("12.50", LocalDate.of(2026, 10, 2), "Groceries"),
                expense("7.25", LocalDate.of(2026, 10, 5), "Transport"),
                expense("0.25", LocalDate.of(2026, 10, 8), "Other")
        );

        assertEquals(new BigDecimal("20.00"), service.calculateTotal(expenses));
    }

    @Test
    void calculateTotalReturnsZeroForEmptyExpenseList() {
        assertEquals(BigDecimal.ZERO, service.calculateTotal(List.of()));
    }

    @Test
    void calculateMonthlySpendingIncludesOnlySelectedMonth() {
        List<Expense> expenses = List.of(
                expense("100.00", LocalDate.of(2026, 10, 2), "Groceries"),
                expense("50.00", LocalDate.of(2026, 10, 15), "Transport"),
                expense("999.00", LocalDate.of(2026, 9, 20), "Other"),
                expense("200.00", LocalDate.of(2026, 11, 1), "Groceries")
        );

        assertEquals(new BigDecimal("150.00"),
                service.calculateMonthlySpending(expenses, YearMonth.of(2026, 10)));
    }

    @Test
    void calculateMonthlySpendingReturnsZeroWhenMonthHasNoExpenses() {
        List<Expense> expenses = List.of(
                expense("45.00", LocalDate.of(2026, 9, 20), "Groceries")
        );

        assertEquals(BigDecimal.ZERO,
                service.calculateMonthlySpending(expenses, YearMonth.of(2026, 10)));
    }

    @Test
    void spendingByCategoryAggregatesAmountsForSelectedMonth() {
        List<Expense> expenses = List.of(
                expense("12.50", LocalDate.of(2026, 10, 2), "Groceries"),
                expense("7.50", LocalDate.of(2026, 10, 12), "Groceries"),
                expense("3.25", LocalDate.of(2026, 10, 15), "Transport"),
                expense("100.00", LocalDate.of(2026, 9, 20), "Groceries")
        );

        Map<String, BigDecimal> expected = Map.of(
                "Groceries", new BigDecimal("20.00"),
                "Transport", new BigDecimal("3.25")
        );

        assertEquals(expected,
                service.spendingByCategory(expenses, YearMonth.of(2026, 10)));
    }

    @Test
    void spendingByCategoryReturnsEmptyMapWhenMonthHasNoExpenses() {
        List<Expense> expenses = List.of(
                expense("45.00", LocalDate.of(2026, 9, 20), "Groceries")
        );

        assertEquals(Map.of(),
                service.spendingByCategory(expenses, YearMonth.of(2026, 10)));
    }
}
