package main.java.expenseTracker.decorator;

import main.java.expenseTracker.model.Expense;

public interface ExpenseProcessor {
    void process(Expense expense);
}