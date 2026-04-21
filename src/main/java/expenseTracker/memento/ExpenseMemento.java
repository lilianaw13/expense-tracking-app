package main.java.expenseTracker.memento;

import main.java.expenseTracker.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseMemento {

    private final List<Expense> savedExpenses;

    public ExpenseMemento(List<Expense> expenses) {
        this.savedExpenses = new ArrayList<>(expenses);
    }

    public List<Expense> getSavedExpenses() {
        return new ArrayList<>(savedExpenses);
    }
}