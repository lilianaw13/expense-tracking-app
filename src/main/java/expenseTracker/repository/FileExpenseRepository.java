package main.java.expenseTracker.repository;

import main.java.expenseTracker.model.Expense;

public class FileExpenseRepository implements IExpenseRepository {

    @Override
    public void save(Expense expense) {
        System.out.println("Expense saved to FILE: " + expense.getAmount());
    }
}
