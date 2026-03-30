package main.java.expenseTracker.decorator;

import main.java.expenseTracker.model.Expense;

public abstract class ExpenseDecorator implements ExpenseProcessor {
    protected ExpenseProcessor wrappedProcessor;

    public ExpenseDecorator(ExpenseProcessor wrappedProcessor) {
        this.wrappedProcessor = wrappedProcessor;
    }

    @Override
    public void process(Expense expense) {
        wrappedProcessor.process(expense);
    }
}