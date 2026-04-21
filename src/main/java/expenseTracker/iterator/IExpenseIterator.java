package main.java.expenseTracker.iterator;

import main.java.expenseTracker.model.Expense;

public interface IExpenseIterator {
    boolean hasNext();
    Expense next();
}