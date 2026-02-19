package main.java.expenseTracker.service;
import main.java.expenseTracker.AbstractFactory.IRepositoryFactory;
import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.repository.ICategoryRepository;
import main.java.expenseTracker.repository.IExpenseRepository;


public class ExpenseService {

    private IExpenseRepository expenseRepository;
    private ICategoryRepository categoryRepository;

    public ExpenseService(IRepositoryFactory factory) {
        this.expenseRepository = factory.createExpenseRepository();
        this.categoryRepository = factory.createCategoryRepository();
    }

    public void saveExpense(Expense expense) {
        expenseRepository.save(expense);
    }
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }
}
