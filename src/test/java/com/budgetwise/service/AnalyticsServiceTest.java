package com.budgetwise.service;

import com.budgetwise.model.Expense;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
}
