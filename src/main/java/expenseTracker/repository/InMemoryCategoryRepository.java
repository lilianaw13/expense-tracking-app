package main.java.expenseTracker.repository;

import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.repository.ICategoryRepository;

public class InMemoryCategoryRepository implements ICategoryRepository {

    @Override
    public void save(Category category) {
        System.out.println("Category saved in memory: " + category.getName());
    }
}
