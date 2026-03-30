package main.java.expenseTracker.bridge;

public abstract class ExpenseReport {
    protected ReportRenderer renderer;

    public ExpenseReport(ReportRenderer renderer) {
        this.renderer = renderer;
    }

    public abstract void generateReport();
}