package com.example.familybudget;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FinanceDB {
    static Connection con;

    static void createOrConnect() {
        if (con == null) {
            try {
                con = DriverManager.getConnection("jdbc:sqlite:familyBudget.db");
                Statement stmt = con.createStatement();
                System.out.print("Database connection established");
    
                // Создание таблицы categories
                String sqlCategories = "CREATE TABLE IF NOT EXISTS categories (" +
                        "category_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "category_name TEXT NOT NULL UNIQUE," +
                        "description TEXT," +
                        "monthly_limit INTEGER NOT NULL," +
                        "notes TEXT);";
                stmt.execute(sqlCategories);
    
                // Создание таблицы expenses
                String sqlExpenses = "CREATE TABLE IF NOT EXISTS expenses (" +
                        "expense_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "category_id INTEGER NOT NULL," +
                        "expense_name TEXT NOT NULL," +
                        "amount REAL NOT NULL," +
                        "date TEXT DEFAULT CURRENT_DATE," +
                        "FOREIGN KEY (category_id) REFERENCES categories(category_id));";
                stmt.execute(sqlExpenses);
    
                System.out.println("Tables created successfully");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    

    // Методы для работы с категориями
    public ArrayList<BudgetCategory> selectCategories() throws SQLException {
        ArrayList<BudgetCategory> categories = new ArrayList<>();

        String sql = "SELECT category_name, description, monthly_limit, notes FROM categories";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String name = rs.getString("category_name");
            String description = rs.getString("description");
            int limit = rs.getInt("monthly_limit");
            String notes = rs.getString("notes");
            categories.add(new BudgetCategory(name, description, limit, notes));
        }
        return categories;
    }

    public void insertCategory(String name, String description, Integer limit, String notes) throws SQLException {
        String sql = "INSERT INTO categories (category_name, description, monthly_limit, notes) VALUES (?, ?, ?, ?)";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setString(2, description);
        pstmt.setInt(3, limit);
        pstmt.setString(4, notes);
        pstmt.executeUpdate();
        pstmt.close();

        System.out.println("Category added successfully");
    }

    public void deleteCategory(String categoryName) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_name = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, categoryName);
        pstmt.executeUpdate();
        pstmt.close();

        System.out.println("Category deleted successfully");
    }

    public void updateCategory(BudgetCategory category, String oldName) throws SQLException {
        String sql = "UPDATE categories SET category_name = ?, description = ?, monthly_limit = ?, notes = ? " +
                "WHERE category_name = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, category.getCategoryName());
        pstmt.setString(2, category.getDescription());
        pstmt.setInt(3, category.getMonthlyLimit());
        pstmt.setString(4, category.getNotes());
        pstmt.setString(5, oldName);
        pstmt.executeUpdate();
        pstmt.close();

        System.out.println("Category updated successfully");
    }

    // Методы для работы с расходами
    public ArrayList<Expense> selectExpenseData(String categoryName) throws SQLException {
        ArrayList<Expense> expenses = new ArrayList<>();

        String sql = "SELECT e.expense_name, e.amount " +
                "FROM expenses e " +
                "JOIN categories c ON e.category_id = c.category_id " +
                "WHERE c.category_name = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, categoryName);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            String name = rs.getString("expense_name");
            double amount = rs.getDouble("amount");
            expenses.add(new Expense(name, amount));
        }
        return expenses;
    }

    public void insertExpense(String name, double amount, BudgetCategory category) throws SQLException {
        String sql = "INSERT INTO expenses (category_id, expense_name, amount) " +
                "VALUES ((SELECT category_id FROM categories WHERE category_name = ?), ?, ?)";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, category.getCategoryName());
        pstmt.setString(2, name);
        pstmt.setDouble(3, amount);
        pstmt.executeUpdate();
        pstmt.close();

        System.out.println("Expense added successfully");
    }

    public void deleteExpense(String expenseName) throws SQLException {
        String sql = "DELETE FROM expenses WHERE expense_name = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, expenseName);
        pstmt.executeUpdate();
        pstmt.close();

        System.out.println("Expense deleted successfully");
    }
}