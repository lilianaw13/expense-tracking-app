package main.java.expenseTracker.builder;

import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.model.Expense;

public class ExpenseDirector {

    private IBuilder builder;

    public ExpenseDirector(IBuilder builder) {
        this.builder = builder;
    }

    public void changeBuilder(IBuilder builder) {
        this.builder = builder;
    }

    public Expense makeSimple(double amount, Category category, String description) {

        builder.reset();
        builder.buildAmount(amount);
        builder.buildCategory(category);
        builder.buildDescription(description);

        return builder.getResult();
    }
}