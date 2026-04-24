package main.java.expenseTracker.visitor;

import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.model.Category;

// Visitor interface — declara o metoda Visit per tip de element vizitat
// Double dispatch: element.accept(visitor) -> visitor.visit(element)
public interface IExpenseVisitor {
    void visit(Expense expense);
    void visit(Category category);
}