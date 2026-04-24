package main.java.expenseTracker.ChainOfResponsibility;

import main.java.expenseTracker.model.Expense;

public abstract class BaseExpenseHandler implements IExpenseHandler {

    private IExpenseHandler next;

    @Override
    public IExpenseHandler setNext(IExpenseHandler next) {
        this.next = next;
        return next; // returneaza next pentru inlantuire fluenta: h1.setNext(h2).setNext(h3)
    }

    // Metoda helper: paseaza la urmatorul sau aproba daca e capatul lantului
    protected String passToNext(Expense expense) {
        if (next != null) return next.handle(expense);
        return "[Chain] APPROVED: '" + expense.getDescription() + "' passed all validations.";
    }
}