package com.budgetwise.service;

import com.budgetwise.model.Expense;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ForecastingServiceTest {

    private final ForecastingService service = new ForecastingService();

    private Expense expense(
            String amount, LocalDate date) {
        return new Expense(
                1L,
                new BigDecimal(amount),
                "Food",
                date,
                "Test expense"
        );
    }

    @Test
    void forecastsSpendingForRemainingDaysOfMonth() {
        List<Expense> expenses = List.of(
                expense("300.00", LocalDate.of(2026, 10, 10))
        );

        BigDecimal result = service.forecastEndOfMonth(
                expenses, LocalDate.of(2026, 10, 10));

        assertEquals(new BigDecimal("930.00"), result);
    }

    @Test
    void ignoresExpensesFromOtherMonths() {
        List<Expense> expenses = List.of(
                expense("300.00", LocalDate.of(2026, 10, 10)),
                expense("500.00", LocalDate.of(2026, 9, 20))
        );

        BigDecimal result = service.forecastEndOfMonth(
                expenses, LocalDate.of(2026, 10, 10));

        assertEquals(new BigDecimal("930.00"), result);
    }

    @Test
    void ignoresExpensesAfterForecastDate() {
        List<Expense> expenses = List.of(
                expense("100.00", LocalDate.of(2026, 10, 5)),
                expense("900.00", LocalDate.of(2026, 10, 20))
        );

        BigDecimal result = service.forecastEndOfMonth(
                expenses, LocalDate.of(2026, 10, 10));

        assertEquals(new BigDecimal("310.00"), result);
    }

    @Test
    void returnsZeroWhenThereAreNoExpenses() {
        BigDecimal result = service.forecastEndOfMonth(
                List.of(), LocalDate.of(2026, 10, 10));

        assertEquals(new BigDecimal("0.00"), result);
    }
}

