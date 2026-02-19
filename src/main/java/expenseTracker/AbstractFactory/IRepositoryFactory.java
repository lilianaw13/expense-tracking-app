package main.java.expenseTracker.AbstractFactory;

import main.java.expenseTracker.repository.IExpenseRepository;
import main.java.expenseTracker.repository.ICategoryRepository;

public interface IRepositoryFactory {

    IExpenseRepository createExpenseRepository();

    ICategoryRepository createCategoryRepository();
}
