package main.java.expenseTracker;

import main.java.expenseTracker.UI.ExpenseTrackerUI;

import javax.swing.*;

public class MainUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpenseTrackerUI ui = new ExpenseTrackerUI();
            ui.setVisible(true);
        });
    }
}