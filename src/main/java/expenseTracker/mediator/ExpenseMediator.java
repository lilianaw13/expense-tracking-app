package main.java.expenseTracker.mediator;

import main.java.expenseTracker.model.Expense;

// Mediator concret — orchestreaza comunicarea intre componente
// Componentele nu se cunosc intre ele — comunica exclusiv prin acest mediator
public class ExpenseMediator implements IExpenseMediator {

    private final ExpenseInputComponent inputComponent;
    private final BudgetTrackerComponent budgetTracker;
    private final NotificationComponent notification;
    private final ReportComponent report;

    public ExpenseMediator(ExpenseInputComponent input,
                           BudgetTrackerComponent budget,
                           NotificationComponent notification,
                           ReportComponent report) {
        this.inputComponent  = input;
        this.budgetTracker   = budget;
        this.notification    = notification;
        this.report          = report;

        // Inregistreaza mediatorul in fiecare componenta
        input.setMediator(this);
        budget.setMediator(this);
        notification.setMediator(this);
        report.setMediator(this);
    }

    @Override
    public void notify(MediatorComponent sender, String event, Object data) {
        switch (event) {
            case "EXPENSE_SUBMITTED":
                Expense expense = (Expense) data;
                notification.sendInfo("Processing: " + expense.getDescription());
                boolean withinBudget = budgetTracker.trackExpense(expense);
                if (withinBudget) {
                    report.logExpense(expense);
                } else {
                    notification.sendAlert("Budget exceeded! Expense rejected: " + expense.getDescription());
                }
                break;

            case "BUDGET_WARNING":
                notification.sendAlert(String.format("Budget WARNING: %.1f%% used!", (double) data));
                break;

            case "BUDGET_CRITICAL":
                notification.sendAlert(String.format("Budget CRITICAL: %.1f%% used! Immediate action required!", (double) data));
                break;

            default:
                System.out.println("[Mediator] Unknown event: " + event);
        }
    }
}