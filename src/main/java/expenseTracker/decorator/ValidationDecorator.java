package main.java.expenseTracker.decorator;

import main.java.expenseTracker.model.Expense;

public class ValidationDecorator extends ExpenseDecorator {

    public ValidationDecorator(ExpenseProcessor wrappedProcessor) {
        super(wrappedProcessor);
    }

    @Override
    public void process(Expense expense) {
        if (expense.getAmount() <= 0) {
            System.out.println("Validation failed: amount must be greater than 0.");
            return;
        }
        if (expense.getCategory() == null) {
            System.out.println("Validation failed: category is required.");
            return;
        }

        System.out.println("Validation passed.");
        super.process(expense);
    }
}