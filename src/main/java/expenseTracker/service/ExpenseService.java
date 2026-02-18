
package main.java.expenseTracker.service;
import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.repository.IExpenseRepository;

public class ExpenseService {

    private IExpenseRepository repository;

    public ExpenseService(IExpenseRepository repository) {
        this.repository = repository;
    }

    public void addExpense(Expense expense) {
        repository.save(expense);
    }
}
