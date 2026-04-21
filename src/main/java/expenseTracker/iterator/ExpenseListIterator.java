package main.java.expenseTracker.iterator;

import main.java.expenseTracker.model.Expense;

import java.util.List;

public class ExpenseListIterator implements IExpenseIterator {

    private List<Expense> expenses;
    private int position = 0;

    public ExpenseListIterator(List<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public boolean hasNext() {
        return position < expenses.size();
    }

    @Override
    public Expense next() {
        return expenses.get(position++);
    }
}