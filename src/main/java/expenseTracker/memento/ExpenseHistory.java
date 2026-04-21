package main.java.expenseTracker.memento;

import java.util.Stack;

public class ExpenseHistory {

    private Stack<ExpenseMemento> history = new Stack<>();

    public void saveState(ExpenseMemento memento) {
        history.push(memento);
    }

    public ExpenseMemento undo() {
        if (!history.isEmpty()) {
            return history.pop();
        }
        return null;
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }
}