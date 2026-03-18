package main.java.expenseTracker.composite;

public class ExpenseLeaf implements IExpenseComponent {

    private String description;
    private double amount;

    public ExpenseLeaf(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    @Override
    public double getTotalAmount() {
        return amount;
    }

    @Override
    public void print() {
        System.out.println(description + " : " + amount);
    }
}