package com.example.familybudget;

public class BudgetCategory {
    private String categoryName;
    private String description;
    private int monthlyLimit;
    private String notes = "";

    public BudgetCategory(String categoryName, String description, int monthlyLimit, String notes) {
        this.categoryName = categoryName;
        this.description = description;
        this.monthlyLimit = monthlyLimit;
        this.notes = notes;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(Integer monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}