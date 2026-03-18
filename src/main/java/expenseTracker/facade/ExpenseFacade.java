package main.java.expenseTracker.facade;
import main.java.expenseTracker.facade.ExpenseReportService;
import main.java.expenseTracker.facade.ExpenseValidator;
import main.java.expenseTracker.composite.ExpenseLeaf;
import java.util.ArrayList;
import java.util.List;

public class ExpenseFacade {

    private ExpenseValidator validator;
    private ExpenseReportService reportService;
    private List<ExpenseLeaf> expenses;

    public ExpenseFacade() {

        validator = new ExpenseValidator();
        reportService = new ExpenseReportService();
        expenses = new ArrayList<>();
    }

    public void addExpense(ExpenseLeaf expense) {

        validator.validate(expense);

        expenses.add(expense);
    }

    public void printReport() {

        double total = reportService.calculateTotal(expenses);

        System.out.println("Total expenses: " + total);
    }
}