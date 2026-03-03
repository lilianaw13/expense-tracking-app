package main.java.expenseTracker.builder;

import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.model.Expense;

public interface IBuilder {

    void reset();

    void buildAmount(double amount);

    void buildCategory(Category category);

    void buildDescription(String description);

    Expense getResult();
}