package com.budgetwise;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    private final String backgroundColor = "#F5F7FB";
    private final String textColor = "#202938";
    private final String mutedColor = "#778196";
    private final String accentColor = "#5B67F1";

    private final ObservableList<ExpenseRow> expenses =
            FXCollections.observableArrayList();

    private final BorderPane root = new BorderPane();

    private Label totalExpensesValue;
    private Label monthlyExpensesValue;
    private Label remainingBudgetValue;

    private final double monthlyBudget = 1000.00;

    @Override
    public void start(Stage stage) {

        // Sample expenses to demonstrate the application.
        // These are temporary and will disappear when the app closes.
        expenses.addAll(
                new ExpenseRow("Groceries", "Food",
                        LocalDate.now().toString(), 45.00),
                new ExpenseRow("Metro ticket", "Transport",
                        LocalDate.now().minusDays(1).toString(), 12.00),
                new ExpenseRow("Restaurant", "Food",
                        LocalDate.now().minusDays(2).toString(), 28.00)
        );

        root.setStyle("-fx-background-color: " + backgroundColor + ";");
        root.setLeft(createSidebar());

        showDashboard();

        Scene scene = new Scene(root, 1200, 780);

        stage.setTitle("BudgetWise | Personal Finance");
        stage.setMinWidth(950);
        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.show();
    }

    // ---------------- SIDEBAR AND NAVIGATION ----------------

    private VBox createSidebar() {

        VBox sidebar = new VBox(18);
        sidebar.setPadding(new Insets(30, 20, 25, 20));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #20283B;");

        Label logo = new Label("◈ BudgetWise");
        logo.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;"
        );

        Label menuLabel = new Label("MAIN MENU");
        menuLabel.setStyle(
                "-fx-text-fill: #9BA6BC;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;"
        );

        Button dashboardButton = createNavButton("Dashboard");
        Button expensesButton = createNavButton("Expenses");
        Button budgetsButton = createNavButton("Budgets");
        Button analyticsButton = createNavButton("Analytics");

        dashboardButton.setOnAction(event -> showDashboard());
        expensesButton.setOnAction(event -> showExpenses());
        budgetsButton.setOnAction(event -> showBudgets());
        analyticsButton.setOnAction(event -> showAnalytics());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label footer = new Label("Manage your money\nwith confidence.");
        footer.setStyle("-fx-text-fill: #AAB4C7; -fx-font-size: 12px;");

        sidebar.getChildren().addAll(
                logo,
                new Region(),
                menuLabel,
                dashboardButton,
                expensesButton,
                budgetsButton,
                analyticsButton,
                spacer,
                footer
        );

        return sidebar;
    }

    private Button createNavButton(String text) {

        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(13, 12, 13, 12));

        button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #D1D8E6;" +
                "-fx-background-radius: 8;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;"
        );

        return button;
    }

    // ---------------- DASHBOARD ----------------

    private void showDashboard() {

        VBox dashboard = createPage("Dashboard",
                "Here's an overview of your finances.");

        HBox summaryCards = createSummaryCards();

        HBox middleSection = createMiddleSection();

        VBox recentExpenses = createRecentExpenses();

        dashboard.getChildren().addAll(
                summaryCards,
                middleSection,
                recentExpenses
        );

        root.setCenter(dashboard);
    }

    private VBox createPage(String title, String subtitle) {

        VBox page = new VBox(22);
        page.setPadding(new Insets(30));
        page.setFillWidth(true);

        Label heading = new Label(title);
        heading.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + textColor + ";"
        );

        Label description = new Label(subtitle);
        description.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + mutedColor + ";"
        );

        VBox header = new VBox(6, heading, description);
        page.getChildren().add(header);

        return page;
    }

    private HBox createSummaryCards() {

        totalExpensesValue = new Label();
        monthlyExpensesValue = new Label();
        remainingBudgetValue = new Label();

        VBox totalCard = createSummaryCard(
                "TOTAL EXPENSES",
                totalExpensesValue,
                "All recorded expenses"
        );

        VBox monthlyCard = createSummaryCard(
                "THIS MONTH",
                monthlyExpensesValue,
                "Expenses this month"
        );

        VBox remainingCard = createSummaryCard(
                "BUDGET REMAINING",
                remainingBudgetValue,
                "From your monthly budget"
        );

        HBox cards = new HBox(18, totalCard, monthlyCard, remainingCard);

        for (var card : cards.getChildren()) {
            HBox.setHgrow(card, Priority.ALWAYS);
        }

        updateSummaryCards();

        return cards;
    }

    private VBox createSummaryCard(
            String title,
            Label amount,
            String description
    ) {

        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setPrefHeight(125);
        card.setMaxWidth(Double.MAX_VALUE);

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E8EBF2;" +
                "-fx-border-radius: 12;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + mutedColor + ";"
        );

        amount.setStyle(
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + textColor + ";"
        );

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: " + mutedColor + ";"
        );

        card.getChildren().addAll(titleLabel, amount, descriptionLabel);

        return card;
    }

    private void updateSummaryCards() {

        double total = expenses.stream()
                .mapToDouble(ExpenseRow::getAmount)
                .sum();

        YearMonth currentMonth = YearMonth.now();

        double monthlyTotal = expenses.stream()
                .filter(expense ->
                        YearMonth.from(
                                LocalDate.parse(expense.getDate())
                        ).equals(currentMonth))
                .mapToDouble(ExpenseRow::getAmount)
                .sum();

        double remaining = monthlyBudget - monthlyTotal;

        if (totalExpensesValue != null) {
            totalExpensesValue.setText(formatMoney(total));
            monthlyExpensesValue.setText(formatMoney(monthlyTotal));
            remainingBudgetValue.setText(formatMoney(remaining));
        }
    }

    private HBox createMiddleSection() {

        VBox chartCard = new VBox(15);
        chartCard.setPadding(new Insets(20));
        chartCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E8EBF2;" +
                "-fx-border-radius: 12;"
        );

        Label chartTitle = new Label("Spending by Category");
        chartTitle.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        PieChart chart = createCategoryChart();
        chart.setPrefHeight(230);

        chartCard.getChildren().addAll(chartTitle, chart);

        VBox budgetCard = new VBox(18);
        budgetCard.setPadding(new Insets(20));
        budgetCard.setPrefWidth(280);
        budgetCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E8EBF2;" +
                "-fx-border-radius: 12;"
        );

        Label budgetTitle = new Label("Monthly Budget");
        budgetTitle.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        Label budgetAmount = new Label();
        Label percentage = new Label();

        ProgressBar progress = new ProgressBar();
        progress.setMaxWidth(Double.MAX_VALUE);

        double monthlyTotal = getMonthlyTotal();
        double ratio = monthlyTotal / monthlyBudget;

        progress.setProgress(Math.min(1.0, Math.max(0.0, ratio)));

        budgetAmount.setText(
                formatMoney(monthlyTotal) + " of " +
                formatMoney(monthlyBudget)
        );

        percentage.setText(
                String.format("%.1f%% of budget used", ratio * 100)
        );

        budgetCard.getChildren().addAll(
                budgetTitle, budgetAmount, progress, percentage
        );

        HBox section = new HBox(18, chartCard, budgetCard);
        HBox.setHgrow(chartCard, Priority.ALWAYS);

        return section;
    }

    private PieChart createCategoryChart() {

        Map<String, Double> categoryTotals = new HashMap<>();

        for (ExpenseRow expense : expenses) {
            categoryTotals.merge(
                    expense.getCategory(),
                    expense.getAmount(),
                    Double::sum
            );
        }

        PieChart chart = new PieChart();

        categoryTotals.forEach((category, amount) ->
                chart.getData().add(new PieChart.Data(category, amount))
        );

        chart.setLabelsVisible(false);
        chart.setLegendVisible(true);

        if (chart.getData().isEmpty()) {
            chart.setTitle("No expenses yet");
        }

        return chart;
    }

    private VBox createRecentExpenses() {

        VBox container = new VBox(12);
        container.setPadding(new Insets(20));
        container.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E8EBF2;" +
                "-fx-border-radius: 12;"
        );

        Label title = new Label("Recent Expenses");
        title.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        TableView<ExpenseRow> table = createExpenseTable();
        table.setPrefHeight(170);

        container.getChildren().addAll(title, table);

        return container;
    }

    // ---------------- EXPENSE TABLE ----------------

    private TableView<ExpenseRow> createExpenseTable() {

        TableView<ExpenseRow> table = new TableView<>();
        table.setItems(expenses);

        TableColumn<ExpenseRow, String> descriptionColumn =
                new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(
                data -> data.getValue().descriptionProperty()
        );

        TableColumn<ExpenseRow, String> categoryColumn =
                new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(
                data -> data.getValue().categoryProperty()
        );

        TableColumn<ExpenseRow, String> dateColumn =
                new TableColumn<>("Date");
        dateColumn.setCellValueFactory(
                data -> data.getValue().dateProperty()
        );

        TableColumn<ExpenseRow, String> amountColumn =
                new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(
                data -> data.getValue().amountTextProperty()
        );

        table.getColumns().addAll(
                descriptionColumn,
                categoryColumn,
                dateColumn,
                amountColumn
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setPlaceholder(new Label("No expenses recorded yet."));

        return table;
    }

    // ---------------- EXPENSES SCREEN ----------------

    private void showExpenses() {

        VBox page = createPage(
                "Expenses",
                "Record and review your spending."
        );

        Label formTitle = new Label("Add a New Expense");
        formTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("e.g. Groceries");

        TextField amountField = new TextField();
        amountField.setPromptText("e.g. 45.50");

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(
                "Food", "Transport", "Shopping",
                "Bills", "Entertainment", "Health", "Other"
        );
        categoryBox.setPromptText("Choose a category");
        categoryBox.setMaxWidth(Double.MAX_VALUE);

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setMaxWidth(Double.MAX_VALUE);

        Button addButton = new Button("Add Expense");
        addButton.setStyle(
                "-fx-background-color: " + accentColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 20 10 20;" +
                "-fx-background-radius: 7;" +
                "-fx-cursor: hand;"
        );

        Label message = new Label();

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);
        form.setPadding(new Insets(20));

        form.add(new Label("Description"), 0, 0);
        form.add(descriptionField, 1, 0);

        form.add(new Label("Amount (€)"), 0, 1);
        form.add(amountField, 1, 1);

        form.add(new Label("Category"), 0, 2);
        form.add(categoryBox, 1, 2);

        form.add(new Label("Date"), 0, 3);
        form.add(datePicker, 1, 3);

        ColumnConstraints labelColumn = new ColumnConstraints();
        ColumnConstraints inputColumn = new ColumnConstraints();
        inputColumn.setHgrow(Priority.ALWAYS);
        inputColumn.setFillWidth(true);

        form.getColumnConstraints().addAll(labelColumn, inputColumn);

        for (var node : form.getChildren()) {
            if (node instanceof TextField field) {
                field.setMaxWidth(Double.MAX_VALUE);
            }
        }

        VBox formCard = new VBox(15, formTitle, form, addButton, message);
        formCard.setPadding(new Insets(22));
        formCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E8EBF2;" +
                "-fx-border-radius: 12;"
        );

        Label listTitle = new Label("All Expenses");
        listTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        TableView<ExpenseRow> table = createExpenseTable();
        table.setPrefHeight(300);

        addButton.setOnAction(event -> {

            String description = descriptionField.getText().trim();
            String amountText = amountField.getText().trim();
            String category = categoryBox.getValue();
            LocalDate date = datePicker.getValue();

            if (description.isEmpty() ||
                    amountText.isEmpty() ||
                    category == null ||
                    date == null) {

                message.setStyle("-fx-text-fill: #D32F2F;");
                message.setText("Please complete all fields.");
                return;
            }

            double amount;

            try {
                amount = Double.parseDouble(amountText.replace(',', '.'));

                if (!Double.isFinite(amount) || amount <= 0) {
                    message.setStyle("-fx-text-fill: #D32F2F;");
                    message.setText("Enter an amount greater than zero.");
                    return;
                }

            } catch (NumberFormatException exception) {
                message.setStyle("-fx-text-fill: #D32F2F;");
                message.setText("Enter a valid amount, e.g. 25.50.");
                return;
            }

            expenses.add(new ExpenseRow(
                    description, category, date.toString(), amount
            ));

            updateSummaryCards();

            message.setStyle("-fx-text-fill: #16834A;");
            message.setText("Expense added successfully.");

            descriptionField.clear();
            amountField.clear();
            categoryBox.setValue(null);
            datePicker.setValue(LocalDate.now());
        });

        page.getChildren().addAll(formCard, listTitle, table);

        root.setCenter(page);
    }

    // ---------------- BUDGETS SCREEN ----------------

    private void showBudgets() {

        VBox page = createPage(
                "Budgets",
                "Monitor your spending against your monthly budget."
        );

        double spent = getMonthlyTotal();
        double remaining = monthlyBudget - spent;
        double ratio = spent / monthlyBudget;

        VBox budgetCard = new VBox(18);
        budgetCard.setPadding(new Insets(25));
        budgetCard.setMaxWidth(550);
        budgetCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E8EBF2;" +
                "-fx-border-radius: 12;"
        );

        Label title = new Label("Monthly Spending Budget");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label budgetLabel = new Label(
                "Budget: " + formatMoney(monthlyBudget)
        );

        Label spentLabel = new Label("Spent: " + formatMoney(spent));

        Label remainingLabel = new Label(
                "Remaining: " + formatMoney(remaining)
        );

        ProgressBar progress = new ProgressBar(
                Math.min(1.0, Math.max(0.0, ratio))
        );
        progress.setMaxWidth(Double.MAX_VALUE);

        Label percentageLabel = new Label(
                String.format("%.1f%% used", ratio * 100)
        );

        budgetCard.getChildren().addAll(
                title, budgetLabel, spentLabel,
                remainingLabel, progress, percentageLabel
        );

        page.getChildren().add(budgetCard);
        root.setCenter(page);
    }

    // ---------------- ANALYTICS SCREEN ----------------

    private void showAnalytics() {

        VBox page = createPage(
                "Analytics",
                "Understand where your money goes."
        );

        Map<String, Double> totals = new HashMap<>();

        for (ExpenseRow expense : expenses) {
            totals.merge(
                    expense.getCategory(),
                    expense.getAmount(),
                    Double::sum
            );
        }

        VBox card = new VBox(18);
        card.setPadding(new Insets(25));
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E8EBF2;" +
                "-fx-border-radius: 12;"
        );

        Label title = new Label("Spending by Category");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        PieChart chart = createCategoryChart();
        chart.setPrefHeight(300);

        card.getChildren().addAll(title, chart);

        Label totalLabel = new Label(
                "Total recorded spending: " +
                formatMoney(expenses.stream()
                        .mapToDouble(ExpenseRow::getAmount)
                        .sum())
        );

        totalLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        page.getChildren().addAll(totalLabel, card);
        root.setCenter(page);
    }

    // ---------------- HELPERS ----------------

    private double getMonthlyTotal() {

        YearMonth currentMonth = YearMonth.now();

        return expenses.stream()
                .filter(expense ->
                        YearMonth.from(
                                LocalDate.parse(expense.getDate())
                        ).equals(currentMonth))
                .mapToDouble(ExpenseRow::getAmount)
                .sum();
    }

    private String formatMoney(double amount) {
        return String.format("€%,.2f", amount);
    }

    // Temporary model for this prototype.
    // We will adapt this to your team's shared Expense model during integration.
    public static class ExpenseRow {

        private final javafx.beans.property.SimpleStringProperty description;
        private final javafx.beans.property.SimpleStringProperty category;
        private final javafx.beans.property.SimpleStringProperty date;
        private final javafx.beans.property.SimpleStringProperty amountText;

        private final double amount;

        public ExpenseRow(
                String description,
                String category,
                String date,
                double amount
        ) {
            this.description =
                    new javafx.beans.property.SimpleStringProperty(description);

            this.category =
                    new javafx.beans.property.SimpleStringProperty(category);

            this.date =
                    new javafx.beans.property.SimpleStringProperty(date);

            this.amount = amount;

            this.amountText =
                    new javafx.beans.property.SimpleStringProperty(
                            String.format("€%,.2f", amount)
                    );
        }

        public String getDescription() {
            return description.get();
        }

        public String getCategory() {
            return category.get();
        }

        public String getDate() {
            return date.get();
        }

        public double getAmount() {
            return amount;
        }

        public javafx.beans.property.StringProperty descriptionProperty() {
            return description;
        }

        public javafx.beans.property.StringProperty categoryProperty() {
            return category;
        }

        public javafx.beans.property.StringProperty dateProperty() {
            return date;
        }

        public javafx.beans.property.StringProperty amountTextProperty() {
            return amountText;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}