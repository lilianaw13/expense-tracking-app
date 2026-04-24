package main.java.expenseTracker.visitor;

import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.model.Category;
import java.util.ArrayList;
import java.util.List;

// Visitor 2 — genereaza raport de audit fara a modifica elementele
public class AuditReportVisitor implements IExpenseVisitor {

    private final List<String> auditLines = new ArrayList<>();
    private int expenseCount = 0;
    private int categoryCount = 0;

    @Override
    public void visit(Expense expense) {
        expenseCount++;
        String flag = expense.getAmount() > 500 ? " *** HIGH VALUE ***" : "";
        String line = String.format("[AUDIT] Expense #%02d: %-20s | %8.2f | %-15s%s",
                expenseCount,
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory().getName(),
                flag);
        auditLines.add(line);
        System.out.println(line);
    }

    @Override
    public void visit(Category category) {
        categoryCount++;
        String line = "[AUDIT] Category #" + categoryCount + ": " + category.getName();
        auditLines.add(line);
        System.out.println(line);
    }

    public void printReport() {
        System.out.println("\n=== AUDIT REPORT SUMMARY ===");
        System.out.println("Total expenses audited : " + expenseCount);
        System.out.println("Total categories scanned: " + categoryCount);
        System.out.println("Total audit entries    : " + auditLines.size());
    }
}