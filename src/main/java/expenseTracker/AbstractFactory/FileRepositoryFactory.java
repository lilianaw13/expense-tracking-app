package main.java.expenseTracker.AbstractFactory;

import main.java.expenseTracker.repository.*;

public class FileRepositoryFactory implements IRepositoryFactory {

    @Override
    public IExpenseRepository createExpenseRepository() {
        return new FileExpenseRepository();
    }

    @Override
    public ICategoryRepository createCategoryRepository() {
        return new FileCategoryRepository();
    }
}