package main.java.expenseTracker.State;

// Stare NORMAL: buget sub 70% utilizat — operatii libere
public class NormalState implements IBudgetState {

    @Override
    public String getStateName() { return "NORMAL"; }

    @Override
    public void addExpense(BudgetAccount account, double amount) {
        account.setSpentAmount(account.getSpentAmount() + amount);
        System.out.printf("[NORMAL] Expense %.2f added. Spent: %.2f / %.2f%n",
                amount, account.getSpentAmount(), account.getTotalBudget());
        updateState(account);
    }

    @Override
    public void printStatus(BudgetAccount account) {
        System.out.printf("[NORMAL] Budget usage: %.1f%% — All good.%n", account.getUsagePercent());
    }

    private void updateState(BudgetAccount account) {
        double pct = account.getUsagePercent();
        if (pct >= 100) account.transitionTo(new ExceededState());
        else if (pct >= 90) account.transitionTo(new CriticalState());
        else if (pct >= 70) account.transitionTo(new WarningState());
    }
}
