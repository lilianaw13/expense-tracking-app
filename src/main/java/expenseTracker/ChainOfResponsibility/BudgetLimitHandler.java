package main.java.expenseTracker.ChainOfResponsibility;

import main.java.expenseTracker.model.Expense;

// Nivel 3: Verifica daca suma nu depaseste limita de buget configurata
public class BudgetLimitHandler extends BaseExpenseHandler {

    private final double budgetLimit;

    public BudgetLimitHandler(double budgetLimit) {
        this.budgetLimit = budgetLimit;
    }

    @Override
    public String handle(Expense expense) {
        System.out.println("[BudgetLimit] Checking against limit " + budgetLimit + "...");
        if (expense.getAmount() > budgetLimit) {
            return "[BudgetLimit] REJECTED: Amount " + expense.getAmount()
                    + " exceeds budget limit " + budgetLimit + ".";
        }
        System.out.println("[BudgetLimit] Within budget limit. Passing to next...");
        return passToNext(expense);
    }
}

