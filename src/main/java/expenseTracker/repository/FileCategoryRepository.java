package main.java.expenseTracker.repository;

import main.java.expenseTracker.model.Category;

public class FileCategoryRepository implements ICategoryRepository {

    @Override
    public void save(Category category) {
        System.out.println("Category saved to file: " + category.getName());
    }
}
