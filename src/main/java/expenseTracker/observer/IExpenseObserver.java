package main.java.expenseTracker.observer;

import main.java.expenseTracker.model.Expense;

public interface IExpenseObserver {
    void update(Expense expense);
}