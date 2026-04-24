package main.java.expenseTracker.visitor;

import main.java.expenseTracker.model.Category;

// Wrapper vizitabil peste Category
public class VisitableCategory implements IVisitable {

    private final Category category;

    public VisitableCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() { return category; }

    @Override
    public void accept(IExpenseVisitor visitor) {
        visitor.visit(category);
    }
}