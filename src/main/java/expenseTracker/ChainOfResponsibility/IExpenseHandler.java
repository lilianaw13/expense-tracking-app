package main.java.expenseTracker.ChainOfResponsibility;

import main.java.expenseTracker.model.Expense;

public interface IExpenseHandler {
    IExpenseHandler setNext(IExpenseHandler next);
    String handle(Expense expense);
}
