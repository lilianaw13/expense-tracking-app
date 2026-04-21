package main.java.expenseTracker.observer;

import main.java.expenseTracker.model.Expense;

public class ExpenseLogObserver implements IExpenseObserver {

    @Override
    public void update(Expense expense) {
        System.out.println("LOG: New expense added -> " +
                expense.getAmount() + " | " +
                expense.getCategory().getName() + " | " +
                expense.getDescription());
    }
}