package com.example.familybudget;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class BudgetViewController {
    FinanceDB adapter = new FinanceDB();

    @FXML
    private TableView<BudgetCategory> tableCategory;
    @FXML
    private TableColumn<BudgetCategory, String> nameCol;
    @FXML
    private TableColumn<BudgetCategory, String> descriptionCol;
    @FXML
    private TableColumn<BudgetCategory, Integer> limitCol;
    @FXML
    private TableColumn<BudgetCategory, String> notesCol;
    @FXML
    private TextField txt_name;
    @FXML
    private TextField txt_description;
    @FXML
    private TextField txt_limit;
    @FXML
    private TextField txt_notes;

    static BudgetCategory currentCategory;

    private void updateTable() throws IOException, SQLException {
        ArrayList<BudgetCategory> data = adapter.selectCategories();
        ObservableList<BudgetCategory> data_new = FXCollections.observableArrayList(data);
        tableCategory.setItems(data_new);
    }

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        limitCol.setCellValueFactory(new PropertyValueFactory<>("monthlyLimit"));
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        FinanceDB.createOrConnect();
        try {
            updateTable();
            System.out.println("Categories table updated successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setupContextMenu();
    }

    @FXML
    public void onAddButtonClick() {
        String name = txt_name.getText();
        String description = txt_description.getText();
        Integer limit = Integer.parseInt(txt_limit.getText());
        String notes = txt_notes.getText();
        try {
            adapter.insertCategory(name, description, limit, notes);
            updateTable();
            System.out.println("Category added successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onDeleteButtonClick() throws IOException, SQLException {
        BudgetCategory selectedCategory = tableCategory.getSelectionModel().getSelectedItem();
        if(selectedCategory != null) {
            try {
                adapter.deleteCategory(selectedCategory.getCategoryName());
                updateTable();
                System.out.println("Category deleted successfully");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void onUpdateButtonClick() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditCategory.fxml"));
        Parent root = loader.load();
        BudgetCategory selectedCategory = tableCategory.getSelectionModel().getSelectedItem();

        Stage dialog = new Stage();
        dialog.setTitle("Редактировать категорию");
        dialog.setScene(new Scene(root, 1100, 300));

        EditCategoryController editController = loader.getController();
        editController.setEditStage(dialog);
        editController.setCategory(selectedCategory);

        dialog.showAndWait();

        if (editController.isSaved()) {
            BudgetCategory newCategory = editController.getCategory();
            String oldName = editController.getOldName();
            adapter.updateCategory(newCategory, oldName);
            updateTable();
        }
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showExpensesItem = new MenuItem("Показать расходы");
    
        showExpensesItem.setOnAction(e -> {
            BudgetCategory selectedCategory = tableCategory.getSelectionModel().getSelectedItem();
            if (selectedCategory != null) {
                currentCategory = selectedCategory;
                showCategoryExpenses();
            }
        });
    
        contextMenu.getItems().add(showExpensesItem);
    
        tableCategory.setContextMenu(contextMenu);
    
        tableCategory.setRowFactory(tv -> {
            TableRow<BudgetCategory> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });
    }

    private void showCategoryExpenses() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("expensesView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Расходы категории: " + currentCategory.getCategoryName());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}