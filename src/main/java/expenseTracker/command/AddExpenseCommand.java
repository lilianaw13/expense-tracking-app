package main.java.expenseTracker.command;

import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.service.ExpenseService;

public class AddExpenseCommand implements IExpenseCommand {

    private ExpenseService expenseService;
    private Expense expense;

    public AddExpenseCommand(ExpenseService expenseService, Expense expense) {
        this.expenseService = expenseService;
        this.expense = expense;
    }

    @Override
    public void execute() {
        expenseService.saveExpense(expense);
    }

    @Override
    public void undo() {
        expenseService.removeExpense(expense);
    }
}