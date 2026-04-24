package main.java.expenseTracker.visitor;

import main.java.expenseTracker.model.Expense;

// Wrapper vizitabil peste Expense — implementeaza accept() fara a modifica clasa Expense
// Respecta OCP: adaugam functionalitate fara a atinge modelul existent
public class VisitableExpense implements IVisitable {

    private final Expense expense;

    public VisitableExpense(Expense expense) {
        this.expense = expense;
    }

    public Expense getExpense() { return expense; }

    // Double dispatch — deleaga la visitor.visit(Expense)
    @Override
    public void accept(IExpenseVisitor visitor) {
        visitor.visit(expense);
    }
}