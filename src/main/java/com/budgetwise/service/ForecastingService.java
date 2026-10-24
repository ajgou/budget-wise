
package com.budgetwise.service;

import com.budgetwise.model.Expense;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

public class ForecastingService {

    /**
     * Estimates total spending by the end of the month,
     * assuming the average daily spending so far continues.
     */
    public BigDecimal forecastEndOfMonth(
            List<Expense> expenses, LocalDate asOfDate) {

        Objects.requireNonNull(expenses, "Expenses cannot be null");
        Objects.requireNonNull(asOfDate, "Date cannot be null");

        YearMonth month = YearMonth.from(asOfDate);

        BigDecimal spentSoFar = expenses.stream()
                .filter(e -> e != null)
                .filter(e -> e.getDate() != null)
                .filter(e -> YearMonth.from(e.getDate()).equals(month))
                .filter(e -> !e.getDate().isAfter(asOfDate))
                .map(Expense::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return spentSoFar
                .multiply(BigDecimal.valueOf(month.lengthOfMonth()))
                .divide(
                        BigDecimal.valueOf(asOfDate.getDayOfMonth()),
                        2,
                        RoundingMode.HALF_UP
                );
    }
}
