package main.java.expenseTracker.observer;

import main.java.expenseTracker.model.Expense;

public class LargeExpenseObserver implements IExpenseObserver {

    @Override
    public void update(Expense expense) {
        if (expense.getAmount() > 1000) {
            System.out.println("ALERT: Large expense detected -> " +
                    expense.getAmount() + " for " + expense.getDescription());
        }
    }
}