package main.java.expenseTracker.bridge;

public class CategoryExpenseReport extends ExpenseReport {

    public CategoryExpenseReport(ReportRenderer renderer) {
        super(renderer);
    }

    @Override
    public void generateReport() {
        renderer.render("Category expense report generated.");
    }
}