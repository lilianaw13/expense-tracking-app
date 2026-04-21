package main.java.expenseTracker.iterator;

import main.java.expenseTracker.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseCollection implements IExpenseCollection {

    private List<Expense> expenses = new ArrayList<>();

    @Override
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    @Override
    public IExpenseIterator createIterator() {
        return new ExpenseListIterator(expenses);
    }
}