package main.java.expenseTracker.service;

import main.java.expenseTracker.AbstractFactory.IRepositoryFactory;
import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.observer.IExpenseObserver;
import main.java.expenseTracker.repository.ICategoryRepository;
import main.java.expenseTracker.repository.IExpenseRepository;
import main.java.expenseTracker.strategy.ISavingStrategy;

import java.util.ArrayList;
import java.util.List;

public class ExpenseService {

    private IExpenseRepository expenseRepository;
    private ICategoryRepository categoryRepository;

    private List<IExpenseObserver> observers = new ArrayList<>();
    private ISavingStrategy savingStrategy;

    public ExpenseService(IRepositoryFactory factory) {
        this.expenseRepository = factory.createExpenseRepository();
        this.categoryRepository = factory.createCategoryRepository();
    }

    public void saveExpense(Expense expense) {
        expenseRepository.save(expense);
        notifyObservers(expense);
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public void removeExpense(Expense expense) {
        expenseRepository.remove(expense);
    }

    public void setAllExpenses(List<Expense> expenses) {
        expenseRepository.setAll(expenses);
    }

    public double getTotalExpenses() {
        double total = 0;
        for (Expense expense : expenseRepository.findAll()) {
            total += expense.getAmount();
        }
        return total;
    }

    public void addObserver(IExpenseObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IExpenseObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Expense expense) {
        for (IExpenseObserver observer : observers) {
            observer.update(expense);
        }
    }

    public void setSavingStrategy(ISavingStrategy savingStrategy) {
        this.savingStrategy = savingStrategy;
    }

    public String getSavingRecommendation(double income) {
        if (savingStrategy == null) {
            return "No saving strategy selected.";
        }
        return savingStrategy.recommend(income, getTotalExpenses());
    }
}