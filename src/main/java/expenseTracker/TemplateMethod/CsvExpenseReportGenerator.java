package main.java.expenseTracker.TemplateMethod;

import main.java.expenseTracker.model.Expense;
import java.util.Comparator;
import java.util.List;

// Subclasa concreta — genereaza raport CSV, sortat dupa suma descrescator
public class CsvExpenseReportGenerator extends ExpenseReportGenerator {

    @Override
    protected String getReportTitle() {
        return "CSV Expense Report";
    }

    // Suprascrie sortarea — ordine descrescatoare dupa suma
    @Override
    protected List<Expense> sortExpenses(List<Expense> expenses) {
        return expenses.stream()
                .sorted(Comparator.comparingDouble(Expense::getAmount).reversed())
                .toList();
    }

    @Override
    protected String formatData(List<Expense> expenses) {
        StringBuilder sb = new StringBuilder();
        sb.append("description,amount,category\n");
        for (Expense e : expenses) {
            sb.append(e.getDescription()).append(",")
                    .append(e.getAmount()).append(",")
                    .append(e.getCategory().getName()).append("\n");
        }
        return sb.toString();
    }

    @Override
    protected void exportReport(String content) {
        System.out.println("[CSV] Exporting to expenses_report.csv:");
        System.out.print(content);
    }

    // Suprascrie hook — actiune dupa export
    @Override
    protected void onAfterExport() {
        System.out.println("[CSV] File ready for upload to server.");
    }
}
