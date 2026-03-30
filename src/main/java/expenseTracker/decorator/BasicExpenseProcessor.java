package main.java.expenseTracker.decorator;

import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.service.ExpenseService;

public class BasicExpenseProcessor implements ExpenseProcessor {
    private ExpenseService expenseService;

    public BasicExpenseProcessor(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Override
    public void process(Expense expense) {
        expenseService.saveExpense(expense);
        System.out.println("Basic processing completed.");
    }
}