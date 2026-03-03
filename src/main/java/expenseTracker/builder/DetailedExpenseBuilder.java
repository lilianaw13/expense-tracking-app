package main.java.expenseTracker.builder;

import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.model.Expense;

public class DetailedExpenseBuilder implements IBuilder {

    private Expense expense;

    public DetailedExpenseBuilder() {
        reset();
    }

    @Override
    public void reset() {
        expense = new Expense(0, null, null);
    }

    @Override
    public void buildAmount(double amount) {
        expense.setAmount(amount * 1.1); // exemplu: adaugă TVA 10%
    }

    @Override
    public void buildCategory(Category category) {
        expense.setCategory(category);
    }

    @Override
    public void buildDescription(String description) {
        expense.setDescription(description + " (detailed)");
    }

    @Override
    public Expense getResult() {
        return expense;
    }
}