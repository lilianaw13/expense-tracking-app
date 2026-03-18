package main.java.expenseTracker.facade;

import main.java.expenseTracker.composite.ExpenseLeaf;

public class ExpenseValidator {

    public void validate(ExpenseLeaf expense) {

        if (expense.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}