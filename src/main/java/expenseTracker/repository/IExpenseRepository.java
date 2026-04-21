package main.java.expenseTracker.repository;

import main.java.expenseTracker.model.Expense;
import java.util.List;

public interface IExpenseRepository {
    void save(Expense expense);
    List<Expense> findAll();
    void remove(Expense expense);
    void setAll(List<Expense> expenses);
}