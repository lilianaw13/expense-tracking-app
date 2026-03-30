package main.java.expenseTracker.UI;

import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.service.ExpenseService;
import main.java.expenseTracker.singleton.AppContext;

import javax.swing.*;
import java.awt.*;

public class ExpenseTrackerUI extends JFrame {

    private ExpenseService expenseService;

    private JTextField amountField;
    private JTextField categoryField;
    private JTextField descriptionField;
    private JTextArea outputArea;

    public ExpenseTrackerUI() {
        expenseService = AppContext.getInstance().getExpenseService();

        setTitle("Expense Tracker App");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField();

        JLabel categoryLabel = new JLabel("Category:");
        categoryField = new JTextField();

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField();

        JButton saveButton = new JButton("Save Expense");
        JButton viewButton = new JButton("View Expenses");

        formPanel.add(amountLabel);
        formPanel.add(amountField);

        formPanel.add(categoryLabel);
        formPanel.add(categoryField);

        formPanel.add(descriptionLabel);
        formPanel.add(descriptionField);

        formPanel.add(saveButton);
        formPanel.add(viewButton);

        add(formPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        saveButton.addActionListener(e -> saveExpense());
        viewButton.addActionListener(e -> viewExpenses());
    }

    private void saveExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String categoryName = categoryField.getText();
            String description = descriptionField.getText();

            Category category = new Category(categoryName);
            Expense expense = new Expense(amount, category, description);

            expenseService.saveCategory(category);
            expenseService.saveExpense(expense);

            outputArea.append("Saved: " + amount + " | " + categoryName + " | " + description + "\n");

            amountField.setText("");
            categoryField.setText("");
            descriptionField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid number.");
        }
    }

    private void viewExpenses() {
        outputArea.append("\n--- All Expenses ---\n");

        for (Expense expense : expenseService.getAllExpenses()) {
            outputArea.append(
                    expense.getAmount() + " | " +
                            expense.getCategory().getName() + " | " +
                            expense.getDescription() + "\n"
            );
        }

        outputArea.append("--------------------\n");
    }
}