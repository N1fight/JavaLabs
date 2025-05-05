package com.example.familybudget;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCategoryController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField limitField;
    @FXML
    private TextField notesField;

    private BudgetCategory category;
    private Stage editStage;
    private boolean saved = false;
    private String oldName;

    public void setEditStage(Stage stage) {
        this.editStage = stage;
    }

    public void setCategory(BudgetCategory category) {
        this.oldName = category.getCategoryName();
        this.category = category;
        nameField.setText(category.getCategoryName());
        descriptionField.setText(category.getDescription());
        limitField.setText(String.valueOf(category.getMonthlyLimit()));
        notesField.setText(category.getNotes());
    }

    public boolean isSaved() {
        return saved;
    }

    public BudgetCategory getCategory() {
        return category;
    }

    public String getOldName() {
        return oldName;
    }

    @FXML
    private void onSave() {
        category.setCategoryName(nameField.getText());
        category.setDescription(descriptionField.getText());
        category.setMonthlyLimit(Integer.parseInt(limitField.getText()));
        category.setNotes(notesField.getText());
        saved = true;
        editStage.close();
    }

    @FXML
    private void onCancel() {
        editStage.close();
    }
}