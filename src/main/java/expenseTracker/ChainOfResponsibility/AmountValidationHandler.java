package main.java.expenseTracker.ChainOfResponsibility;

import main.java.expenseTracker.model.Expense;

// Nivel 1: Verifica daca suma este pozitiva
public class AmountValidationHandler extends BaseExpenseHandler {

    @Override
    public String handle(Expense expense) {
        System.out.println("[AmountValidation] Checking amount: " + expense.getAmount() + "...");
        if (expense.getAmount() <= 0) {
            return "[AmountValidation] REJECTED: Amount must be positive (got " + expense.getAmount() + ").";
        }
        System.out.println("[AmountValidation] Amount OK. Passing to next...");
        return passToNext(expense);
    }
}

