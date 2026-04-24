package main.java.expenseTracker.TemplateMethod;

import main.java.expenseTracker.model.Expense;
import java.util.List;
import java.util.stream.Collectors;

// Subclasa concreta — genereaza raport HTML, filtreaza cheltuielile sub 50
public class HtmlExpenseReportGenerator extends ExpenseReportGenerator {

    @Override
    protected String getReportTitle() {
        return "HTML Expense Report";
    }

    // Suprascrie filtrul — afiseaza doar cheltuielile semnificative (>= 50)
    @Override
    protected List<Expense> filterExpenses(List<Expense> expenses) {
        return expenses.stream()
                .filter(e -> e.getAmount() >= 50)
                .collect(Collectors.toList());
    }

    @Override
    protected String formatData(List<Expense> expenses) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1'>\n");
        sb.append("  <tr><th>Description</th><th>Amount</th><th>Category</th></tr>\n");
        for (Expense e : expenses) {
            sb.append("  <tr><td>").append(e.getDescription()).append("</td>")
                    .append("<td>").append(e.getAmount()).append("</td>")
                    .append("<td>").append(e.getCategory().getName()).append("</td></tr>\n");
        }
        sb.append("</table>");
        return sb.toString();
    }

    @Override
    protected void exportReport(String content) {
        System.out.println("[HTML] Exporting to expenses_report.html:");
        System.out.println(content);
    }

    @Override
    protected void onBeforeExport() {
        System.out.println("[HTML] Applying CSS stylesheet...");
    }
}
