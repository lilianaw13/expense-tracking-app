package main.java.expenseTracker.command;

import java.util.Stack;

public class CommandManager {

    private Stack<IExpenseCommand> history = new Stack<>();

    public void executeCommand(IExpenseCommand command) {
        command.execute();
        history.push(command);
    }

    public void undoLastCommand() {
        if (!history.isEmpty()) {
            IExpenseCommand command = history.pop();
            command.undo();
        } else {
            System.out.println("No command to undo.");
        }
    }
}