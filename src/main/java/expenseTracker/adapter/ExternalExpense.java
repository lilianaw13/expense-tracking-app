package main.java.expenseTracker.adapter;

public class ExternalExpense {
    private String title;
    private double value;

    public ExternalExpense(String title, double value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public double getValue() {
        return value;
    }
}
