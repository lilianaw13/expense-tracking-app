package main.java.expenseTracker.facade;

import main.java.expenseTracker.composite.ExpenseLeaf;
import java.util.List;

public class ExpenseReportService {

    public double calculateTotal(List<ExpenseLeaf> expenses) {

        double total = 0;

        for (ExpenseLeaf e : expenses) {
            total += e.getTotalAmount();
        }

        return total;
    }
}