package main.java.expenseTracker.model;

public class Expense {
    private double amount;
    private Category category;
    private String description;

    public Expense(double amount, Category category, String description) {
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }
}
