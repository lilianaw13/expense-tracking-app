package main.java.expenseTracker.visitor;

import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.model.Category;

// Visitor 1 — calculeaza suma totala a cheltuielilor
// Nu modifica Expense sau Category — operatia e complet separata de elemente
public class TotalAmountVisitor implements IExpenseVisitor {

    private double total = 0;
    private int    count = 0;

    @Override
    public void visit(Expense expense) {
        total += expense.getAmount();
        count++;
        System.out.printf("[TotalVisitor] Visiting: %-20s | %8.2f%n",
                expense.getDescription(), expense.getAmount());
    }

    @Override
    public void visit(Category category) {
        // Categoriile nu au suma — afisam doar numele
        System.out.println("[TotalVisitor] Category visited: " + category.getName());
    }

    public void printSummary() {
        System.out.printf("[TotalVisitor] Total: %.2f across %d expenses.%n", total, count);
    }

    public double getTotal() { return total; }
}