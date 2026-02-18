package main.java.expenseTracker.repository;
import main.java.expenseTracker.model.Expense;

public interface IExpenseRepository {
    void save(Expense expense);
}
