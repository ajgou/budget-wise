
package com.budgetwise.service;

import com.budgetwise.model.Budget;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SavingInsightsServiceTest {

    private final SavingInsightsService service =
            new SavingInsightsService();

    private Budget budget(String limit) {
        return new Budget(
                YearMonth.of(2026, 10),
                new BigDecimal(limit)
        );
    }

    @Test
    void warnsWhenSpendingExceedsBudget() {
        List<String> insights = service.generateInsights(
                budget("1000.00"),
                new BigDecimal("1200.00")
        );

        assertTrue(insights.stream()
                .anyMatch(text -> text.contains("over budget")));
    }

    @Test
    void warnsWhenEightyPercentOfBudgetIsUsed() {
        List<String> insights = service.generateInsights(
                budget("1000.00"),
                new BigDecimal("800.00")
        );

        assertTrue(insights.stream()
                .anyMatch(text -> text.contains("80.00%")));
    }

    @Test
    void reportsRemainingBudgetWhenSpendingIsLow() {
        List<String> insights = service.generateInsights(
                budget("1000.00"),
                new BigDecimal("300.00")
        );

        assertTrue(insights.stream()
                .anyMatch(text -> text.contains("700.00 remaining")));
    }

    @Test
    void rejectsZeroBudget() {
        assertThrows(IllegalArgumentException.class,
                () -> service.generateInsights(
                        budget("0.00"),
                        new BigDecimal("100.00")
                ));
    }
}
