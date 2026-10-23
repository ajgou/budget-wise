package com.budgetwise.database;

import com.budgetwise.model.Expense;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {

    /**
     * Inserts a new expense into the database.
     */
    public static void insertExpense(Expense expense) {
        String sql = "INSERT INTO expenses (amount, category, date, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, expense.getAmount().toPlainString());
            pstmt.setString(2, expense.getCategory());
            pstmt.setString(3, expense.getDate().toString());
            pstmt.setString(4, expense.getDescription());
            
            pstmt.executeUpdate();
            System.out.println("Expense inserted successfully!");
        } catch (SQLException e) {
            System.err.println("Error inserting expense: " + e.getMessage());
        }
    }

    /**
     * Retrieves all expenses from the database.
     */
    public static List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT id, amount, category, date, description FROM expenses";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                long id = rs.getLong("id");
                BigDecimal amount = new BigDecimal(rs.getString("amount"));
                String category = rs.getString("category");
                LocalDate date = LocalDate.parse(rs.getString("date"));
                String description = rs.getString("description");
                
                Expense expense = new Expense(id, amount, category, date, description);
                expenses.add(expense);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving expenses: " + e.getMessage());
        }
        return expenses;
    }

    /**
     * Simple main method to test database operations directly.
     */
    public static void main(String[] args) {
        System.out.println("Testing database operations...");
        
        // Insert a test expense
        Expense testExpense = new Expense(0, new BigDecimal("15.50"), "Food", LocalDate.now(), "Lunch at cafeteria");
        insertExpense(testExpense);
        
        // Print all expenses
        List<Expense> allExpenses = getAllExpenses();
        System.out.println("Current expenses in database:");
        for (Expense e : allExpenses) {
            System.out.println("- " + e.getCategory() + ": $" + e.getAmount() + " on " + e.getDate() + " (" + e.getDescription() + ")");
        }
    }
}