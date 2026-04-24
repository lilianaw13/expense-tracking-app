package main.java.expenseTracker.mediator;

import main.java.expenseTracker.model.Expense;

// Componenta de intrare — utilizatorul trimite cheltuiala PRIN mediator, nu direct
public class ExpenseInputComponent extends MediatorComponent {

    public void submitExpense(Expense expense) {
        System.out.println("[ExpenseInput] User submitted: '"
                + expense.getDescription() + "' | " + expense.getAmount());
        mediator.notify(this, "EXPENSE_SUBMITTED", expense);
    }
}