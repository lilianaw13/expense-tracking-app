package main.java.expenseTracker.builder;
import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.model.Expense;

public class SimpleExpenseBuilder implements IBuilder {

    private Expense expense;

    public SimpleExpenseBuilder() {
        reset();
    }

    @Override
    public void reset() {
        expense = new Expense(0, null, null);
    }

    @Override
    public void buildAmount(double amount) {
        expense.setAmount(amount);
    }

    @Override
    public void buildCategory(Category category) {
        expense.setCategory(category);
    }

    @Override
    public void buildDescription(String description) {
        expense.setDescription(description);
    }

    @Override
    public Expense getResult() {
        return expense;
    }
}