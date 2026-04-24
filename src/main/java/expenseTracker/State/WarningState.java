package main.java.expenseTracker.State;

// Stare WARNING: buget intre 70-90% — avertisment utilizator
public class WarningState implements IBudgetState {

    @Override
    public String getStateName() { return "WARNING"; }

    @Override
    public void addExpense(BudgetAccount account, double amount) {
        account.setSpentAmount(account.getSpentAmount() + amount);
        System.out.printf("[WARNING] ⚠  Expense %.2f added. Spent: %.2f / %.2f%n",
                amount, account.getSpentAmount(), account.getTotalBudget());
        double pct = account.getUsagePercent();
        if (pct >= 100) account.transitionTo(new ExceededState());
        else if (pct >= 90) account.transitionTo(new CriticalState());
    }

    @Override
    public void printStatus(BudgetAccount account) {
        System.out.printf("[WARNING] ⚠  Budget usage: %.1f%% — Consider reducing expenses!%n",
                account.getUsagePercent());
    }
}
