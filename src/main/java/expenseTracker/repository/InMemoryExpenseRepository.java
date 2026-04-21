package main.java.expenseTracker.repository;

import main.java.expenseTracker.model.Expense;
import java.util.ArrayList;
import java.util.List;

public class InMemoryExpenseRepository implements IExpenseRepository {

    private List<Expense> expenses = new ArrayList<>();

    @Override
    public void save(Expense expense) {
        expenses.add(expense);
        System.out.println("Expense saved in memory: " + expense.getAmount());
    }

    @Override
    public List<Expense> findAll() {
        return new ArrayList<>(expenses);
    }

    @Override
    public void remove(Expense expense) {
        expenses.remove(expense);
        System.out.println("Expense removed from memory: " + expense.getAmount());
    }

    @Override
    public void setAll(List<Expense> expenses) {
        this.expenses = new ArrayList<>(expenses);
    }
}