package main.java.expenseTracker.model;
import main.java.expenseTracker.prototype.IPrototype;

public class Expense implements IPrototype<Expense>{
    private double amount;
    private Category category;
    private String description;

    // Constructor gol pentru Builder
    private Expense() {}

    // Constructor existent
    public Expense(double amount, Category category, String description) {
        this.amount = amount;
        this.category = category;
        this.description = description;
    }
    // SETTERS pentru Builder
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
    //  SHALLOW COPY
    @Override
    public Expense copy() {
        return new Expense(this.amount, this.category, this.description);
    }

    //  DEEP COPY
    @Override
    public Expense deepCopy() {

        Category newCategory = new Category(this.category.getName());

        return new Expense(this.amount, newCategory, this.description);
    }
}
