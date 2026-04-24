package main.java.expenseTracker.TemplateMethod;

import main.java.expenseTracker.model.Expense;
import java.util.List;

// Abstract Class — defineste scheletul algoritmului de generare raport
// Metoda generateReport() este FINAL — subclasele nu pot modifica ordinea pasilor
public abstract class ExpenseReportGenerator {

    // Template Method — scheletul fix, apeleaza pasii in ordine
    public final void generateReport(List<Expense> expenses) {
        System.out.println("\n--- " + getReportTitle() + " ---");
        List<Expense> filtered  = filterExpenses(expenses);   // pas 1 — poate fi suprascris
        List<Expense> sorted    = sortExpenses(filtered);      // pas 2 — poate fi suprascris
        String        formatted = formatData(sorted);          // pas 3 — ABSTRACT, obligatoriu
        onBeforeExport();                                       // hook optional
        exportReport(formatted);                               // pas 4 — ABSTRACT, obligatoriu
        onAfterExport();                                        // hook optional
        System.out.println("--- Report done. ---");
    }

    // Pasi abstracti — subclasele TREBUIE sa ii implementeze
    protected abstract String formatData(List<Expense> expenses);
    protected abstract void exportReport(String content);
    protected abstract String getReportTitle();

    // Pasii cu implementare default — subclasele POT sa ii suprascrie
    protected List<Expense> filterExpenses(List<Expense> expenses) {
        return expenses; // default: fara filtrare
    }

    protected List<Expense> sortExpenses(List<Expense> expenses) {
        return expenses; // default: fara sortare
    }

    // Hook-uri optionale — subclasele POT sa le suprascrie
    protected void onBeforeExport() { }
    protected void onAfterExport()  { }
}
