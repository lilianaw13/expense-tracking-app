package main.java.expenseTracker.singleton;

import main.java.expenseTracker.AbstractFactory.IRepositoryFactory;
import main.java.expenseTracker.AbstractFactory.InMemoryRepositoryFactory;
import main.java.expenseTracker.service.ExpenseService;

public class AppContext {

    private static volatile AppContext instance;

    private IRepositoryFactory repositoryFactory;
    private ExpenseService expenseService;

    // constructor privat
    private AppContext() {
        repositoryFactory = new InMemoryRepositoryFactory();
        expenseService = new ExpenseService(repositoryFactory);
    }

    public static AppContext getInstance() {

        if (instance == null) {
            synchronized (AppContext.class) {
                if (instance == null) {
                    instance = new AppContext();
                }
            }
        }

        return instance;
    }

    public ExpenseService getExpenseService() {
        return expenseService;
    }

    public void setRepositoryFactory(IRepositoryFactory factory) {
        this.repositoryFactory = factory;
        this.expenseService = new ExpenseService(factory);
    }
}