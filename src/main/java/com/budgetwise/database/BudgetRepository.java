package com.budgetwise.database;

import com.budgetwise.model.Budget;

import java.math.BigDecimal;
import java.sql.*;
import java.time.YearMonth;

public class BudgetRepository {

    /**
     * Saves or updates a monthly budget in the database.
     */
    public static void saveOrUpdateBudget(Budget budget) {
        String sql = "INSERT INTO budgets (month, monthly_limit) VALUES (?, ?) " +
                     "ON CONFLICT(month) DO UPDATE SET monthly_limit = excluded.monthly_limit";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, budget.getMonth().toString()); // Format: YYYY-MM
            pstmt.setString(2, budget.getMonthlyLimit().toPlainString());
            
            pstmt.executeUpdate();
            System.out.println("Budget saved successfully for " + budget.getMonth());
        } catch (SQLException e) {
            System.err.println("Error saving budget: " + e.getMessage());
        }
    }

    /**
     * Retrieves the budget for a selected month.
     */
    public static Budget getBudgetForMonth(YearMonth month) {
        String sql = "SELECT month, monthly_limit FROM budgets WHERE month = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, month.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    YearMonth dbMonth = YearMonth.parse(rs.getString("month"));
                    BigDecimal limit = new BigDecimal(rs.getString("monthly_limit"));
                    return new Budget(dbMonth, limit);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving budget: " + e.getMessage());
        }
        return null; // Return null if no budget set for this month
    }
}