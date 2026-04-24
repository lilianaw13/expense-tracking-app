package main.java.expenseTracker.ChainOfResponsibility;

import main.java.expenseTracker.model.Expense;

// Nivel 2: Verifica daca categoria este valida
public class CategoryValidationHandler extends BaseExpenseHandler {

    @Override
    public String handle(Expense expense) {
        System.out.println("[CategoryValidation] Checking category...");
        if (expense.getCategory() == null
                || expense.getCategory().getName() == null
                || expense.getCategory().getName().isBlank()) {
            return "[CategoryValidation] REJECTED: Category is missing or empty.";
        }
        System.out.println("[CategoryValidation] Category '" + expense.getCategory().getName() + "' OK. Passing to next...");
        return passToNext(expense);
    }
}
