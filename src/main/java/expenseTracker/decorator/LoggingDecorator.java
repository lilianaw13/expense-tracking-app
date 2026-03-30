package main.java.expenseTracker.decorator;

import main.java.expenseTracker.model.Expense;

public class LoggingDecorator extends ExpenseDecorator {

    public LoggingDecorator(ExpenseProcessor wrappedProcessor) {
        super(wrappedProcessor);
    }

    @Override
    public void process(Expense expense) {
        System.out.println("LOG: Processing expense -> " +
                expense.getDescription() + ", amount: " + expense.getAmount());

        wrappedProcessor.process(expense);

        System.out.println("LOG: Processing finished.");
    }
}