package main.java.expenseTracker.repository;

import main.java.expenseTracker.model.Expense;
import java.util.ArrayList;
import java.util.List;

public class FileExpenseRepository implements IExpenseRepository {

    @Override
    public void save(Expense expense) {
        System.out.println("Expense saved to FILE: " + expense.getAmount());
    }

    @Override
    public List<Expense> findAll() {
        return new ArrayList<>();
    }
}