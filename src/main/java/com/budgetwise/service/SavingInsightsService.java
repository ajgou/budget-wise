
package com.budgetwise.service;

import com.budgetwise.model.Budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SavingInsightsService {

    /**
     * Generates simple, explainable budget insights.
     */
    public List<String> generateInsights(
            Budget budget, BigDecimal monthlySpending) {

        Objects.requireNonNull(budget, "Budget cannot be null");
        Objects.requireNonNull(
                monthlySpending, "Monthly spending cannot be null");

        BigDecimal limit = budget.getMonthlyLimit();

        if (limit == null || limit.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Monthly budget must be greater than zero");
        }

        if (monthlySpending.signum() < 0) {
            throw new IllegalArgumentException(
                    "Monthly spending cannot be negative");
        }

        List<String> insights = new ArrayList<>();

        if (monthlySpending.compareTo(limit) > 0) {
            BigDecimal overBudget = monthlySpending.subtract(limit);

            insights.add("You are over budget by "
                    + overBudget.toPlainString() + ".");
        } else {
            BigDecimal remaining = limit.subtract(monthlySpending);
            BigDecimal usagePercent = monthlySpending
                    .multiply(BigDecimal.valueOf(100))
                    .divide(limit, 2, java.math.RoundingMode.HALF_UP);

            if (usagePercent.compareTo(BigDecimal.valueOf(80)) >= 0) {
                insights.add(
                        "You have used " + usagePercent.toPlainString()
                        + "% of your monthly budget. Consider limiting "
                        + nonEssentialSpending() + ".");
            }

            insights.add(
                    "You have " + remaining.toPlainString()
                    + " remaining in your monthly budget.");
        }

        if (insights.isEmpty()) {
            insights.add("Your spending is within your budget.");
        }

        return insights;
    }

    private String nonEssentialSpending() {
        return "non-essential spending";
    }
}
