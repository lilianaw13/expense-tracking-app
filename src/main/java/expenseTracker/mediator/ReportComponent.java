package main.java.expenseTracker.mediator;

import main.java.expenseTracker.model.Expense;

// Componenta de raportare — logheaza cheltuielile aprobate
public class ReportComponent extends MediatorComponent {

    public void logExpense(Expense expense) {
        System.out.printf("[Report] Logged: %-20s | %8.2f | %s%n",
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory().getName());
    }
}