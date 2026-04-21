package main.java.expenseTracker.iterator;

import main.java.expenseTracker.model.Expense;

public interface IExpenseCollection {
    void addExpense(Expense expense);
    IExpenseIterator createIterator();
}