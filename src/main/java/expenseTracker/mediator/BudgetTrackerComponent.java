package main.java.expenseTracker.mediator;

import main.java.expenseTracker.model.Expense;

// Componenta de tracking buget — notifica mediatorul daca limita e apropiata
public class BudgetTrackerComponent extends MediatorComponent {

    private final double totalBudget;
    private double spentAmount = 0;

    public BudgetTrackerComponent(double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public boolean trackExpense(Expense expense) {
        spentAmount += expense.getAmount();
        double percent = (spentAmount / totalBudget) * 100;
        System.out.printf("[BudgetTracker] Spent: %.2f / %.2f (%.1f%%)%n",
                spentAmount, totalBudget, percent);

        if (percent >= 90) {
            mediator.notify(this, "BUDGET_CRITICAL", percent);
        } else if (percent >= 70) {
            mediator.notify(this, "BUDGET_WARNING", percent);
        }

        return spentAmount <= totalBudget;
    }
}