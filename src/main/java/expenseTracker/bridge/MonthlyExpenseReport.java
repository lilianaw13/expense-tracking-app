package main.java.expenseTracker.bridge;

public class MonthlyExpenseReport extends ExpenseReport {

    public MonthlyExpenseReport(ReportRenderer renderer) {
        super(renderer);
    }

    @Override
    public void generateReport() {
        renderer.render("Monthly expense report generated.");
    }
}