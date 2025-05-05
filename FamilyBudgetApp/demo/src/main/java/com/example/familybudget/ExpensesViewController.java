package com.example.familybudget;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ExpensesViewController extends BudgetViewController {
    @FXML
    private TableView<Expense> tableExpense;
    @FXML
    private TableColumn<Expense, String> expenseNameCol;
    @FXML
    private TableColumn<Expense, Double> amountCol;
    @FXML
    private TextField txt_expense_name;
    @FXML
    private TextField txt_amount;

    private FinanceDB financeDB = new FinanceDB();

    @FXML
    public void initialize() {
        expenseNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        try {
            updateTable();
            System.out.println("Successfully updated expenses table");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateTable() throws IOException, SQLException {
        ArrayList<Expense> data = financeDB.selectExpenseData(BudgetViewController.currentCategory.getCategoryName());
        ObservableList<Expense> data_new = FXCollections.observableArrayList(data);
        tableExpense.setItems(data_new);
    }

    @FXML
    public void onAddExpenseButtonClick() {
        String name = txt_expense_name.getText();
        double amount = Double.parseDouble(txt_amount.getText());
        try {
            financeDB.insertExpense(name, amount, BudgetViewController.currentCategory);
            updateTable();
            System.out.println("Expense added successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onDeleteExpenseButtonClick() throws IOException, SQLException {
        Expense selectedExpense = tableExpense.getSelectionModel().getSelectedItem();
        if(selectedExpense != null) {
            try {
                financeDB.deleteExpense(selectedExpense.getName());
                updateTable();
                System.out.println("Expense deleted successfully");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}