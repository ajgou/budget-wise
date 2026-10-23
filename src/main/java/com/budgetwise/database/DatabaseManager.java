package com.budgetwise.database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_DIR = System.getProperty("user.home") + "/.budgetwise";
    private static final String DB_FILE = DB_DIR + "/budgetwise.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;

    static {
        initializeDatabase();
    }

    /**
     * Returns a new connection to the SQLite database.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Ensures the storage directory exists and initializes the database schema.
     */
    public static void initializeDatabase() {
        try {
            Path dirPath = Paths.get(DB_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                
                // Create expenses table
                String createExpensesTable = "CREATE TABLE IF NOT EXISTS expenses (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "amount TEXT NOT NULL, " +
                        "category TEXT NOT NULL, " +
                        "date TEXT NOT NULL, " +
                        "description TEXT" +
                        ");";

                // Create budgets table
                String createBudgetsTable = "CREATE TABLE IF NOT EXISTS budgets (" +
                        "month TEXT PRIMARY KEY, " +
                        "monthly_limit TEXT NOT NULL" +
                        ");";

                stmt.execute(createExpensesTable);
                stmt.execute(createBudgetsTable);
                
                System.out.println("Database initialized successfully at: " + DB_FILE);

            }
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}