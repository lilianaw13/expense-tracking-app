package main.java.expenseTracker.AbstractFactory;

import main.java.expenseTracker.repository.*;

public class InMemoryRepositoryFactory implements IRepositoryFactory {

    @Override
    public IExpenseRepository createExpenseRepository() {


        return new InMemoryExpenseRepository();
    }

    @Override
    public ICategoryRepository createCategoryRepository() {

        return new InMemoryCategoryRepository();
    }
}
