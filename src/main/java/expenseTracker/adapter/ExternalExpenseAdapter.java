package main.java.expenseTracker.adapter;

import main.java.expenseTracker.composite.ExpenseLeaf;

public class ExternalExpenseAdapter extends ExpenseLeaf {

    private ExternalExpense externalExpense;

    public ExternalExpenseAdapter(ExternalExpense externalExpense) {

        super(externalExpense.getTitle(), externalExpense.getValue());

        this.externalExpense = externalExpense;
    }
}