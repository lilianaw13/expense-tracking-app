package main.java.expenseTracker.flyweight;

import main.java.expenseTracker.model.Category;
import java.util.HashMap;
import java.util.Map;

public class CategoryFlyweightFactory {
    private Map<String, Category> categoryMap = new HashMap<>();

    public Category getCategory(String name) {
        if (!categoryMap.containsKey(name)) {
            categoryMap.put(name, new Category(name));
            System.out.println("Created new category: " + name);
        } else {
            System.out.println("Reused existing category: " + name);
        }
        return categoryMap.get(name);
    }

    public int getCategoryCount() {
        return categoryMap.size();
    }
}