package main.java.expenseTracker.TemplateMethod;

import main.java.expenseTracker.model.Expense;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Subclasa concreta — genereaza raport sumar grupat pe categorii (consolă)
public class SummaryExpenseReportGenerator extends ExpenseReportGenerator {

    @Override
    protected String getReportTitle() {
        return "Summary Expense Report (by Category)";
    }

    @Override
    protected String formatData(List<Expense> expenses) {
        Map<String, Double> byCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().getName(),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        StringBuilder sb = new StringBuilder();
        double total = 0;
        for (Map.Entry<String, Double> entry : byCategory.entrySet()) {
            sb.append(String.format("  %-20s : %8.2f%n", entry.getKey(), entry.getValue()));
            total += entry.getValue();
        }
        sb.append(String.format("  %-20s : %8.2f%n", "TOTAL", total));
        return sb.toString();
    }

    @Override
    protected void exportReport(String content) {
        System.out.println("[Summary] Printing to console:\n" + content);
    }
}
