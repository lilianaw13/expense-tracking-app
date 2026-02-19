package main.java.expenseTracker.repository;
import main.java.expenseTracker.model.Category;
public interface ICategoryRepository {
    void save(Category category);
}
