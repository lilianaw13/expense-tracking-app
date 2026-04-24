package main.java.expenseTracker.ChainOfResponsibility;

import main.java.expenseTracker.model.Expense;

// Nivel 4: Detecteaza cheltuieli suspecte (suma foarte mare)
public class FraudDetectionHandler extends BaseExpenseHandler {

    private static final double FRAUD_THRESHOLD = 5000.0;

    @Override
    public String handle(Expense expense) {
        System.out.println("[FraudDetection] Scanning for suspicious activity...");
        if (expense.getAmount() > FRAUD_THRESHOLD) {
            return "[FraudDetection] FLAGGED: Amount " + expense.getAmount()
                    + " exceeds fraud threshold " + FRAUD_THRESHOLD
                    + " — manual review required!";
        }
        System.out.println("[FraudDetection] No fraud detected.");
        return passToNext(expense);
    }
}