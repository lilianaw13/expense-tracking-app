package main.java.expenseTracker.composite;

import java.util.ArrayList;
import java.util.List;

public class ExpenseGroup implements IExpenseComponent {

    private String name;
    private List<IExpenseComponent> children = new ArrayList<>();

    public ExpenseGroup(String name) {
        this.name = name;
    }

    public void add(IExpenseComponent component) {
        children.add(component);
    }

    public void remove(IExpenseComponent component) {
        children.remove(component);
    }

    @Override
    public double getTotalAmount() {

        double total = 0;

        for (IExpenseComponent component : children) {
            total += component.getTotalAmount();
        }

        return total;
    }

    @Override
    public void print() {

        System.out.println("Group: " + name);

        for (IExpenseComponent component : children) {
            component.print();
        }
    }
}