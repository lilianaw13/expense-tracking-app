package main.java.expenseTracker.command;

public interface IExpenseCommand {
    void execute();
    void undo();
}