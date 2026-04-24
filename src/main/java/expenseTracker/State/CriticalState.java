package main.java.expenseTracker.State;

// Stare CRITICAL: buget intre 90-100% — zona de pericol
public class CriticalState implements IBudgetState {

    @Override
    public String getStateName() { return "CRITICAL"; }

    @Override
    public void addExpense(BudgetAccount account, double amount) {
        account.setSpentAmount(account.getSpentAmount() + amount);
        System.out.printf("[CRITICAL] ⛔ Expense %.2f added. Spent: %.2f / %.2f%n",
                amount, account.getSpentAmount(), account.getTotalBudget());
        if (account.getUsagePercent() >= 100) {
            account.transitionTo(new ExceededState());
        }
    }

    @Override
    public void printStatus(BudgetAccount account) {
        System.out.printf("[CRITICAL] ⛔ Budget usage: %.1f%% — DANGER ZONE!%n",
                account.getUsagePercent());
    }
}
