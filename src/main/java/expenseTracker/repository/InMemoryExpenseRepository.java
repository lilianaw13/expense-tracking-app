package main.java.expenseTracker.repository;
import main.java.expenseTracker.model.Expense;

public class InMemoryExpenseRepository implements IExpenseRepository {

    @Override
    public void save(Expense expense) {
        System.out.println("Expense saved in memory: " + expense.getAmount());
    }
}
