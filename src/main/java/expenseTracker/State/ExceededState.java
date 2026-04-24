package main.java.expenseTracker.State;

// Stare EXCEEDED: buget depasit 100% — cheltuieli blocate
public class ExceededState implements IBudgetState {

    @Override
    public String getStateName() { return "EXCEEDED"; }

    @Override
    public void addExpense(BudgetAccount account, double amount) {
        System.out.printf("[EXCEEDED] 🚫 Cannot add expense %.2f — budget already exceeded!%n", amount);
    }

    @Override
    public void printStatus(BudgetAccount account) {
        System.out.printf("[EXCEEDED] 🚫 Budget EXCEEDED: %.2f spent of %.2f total.%n",
                account.getSpentAmount(), account.getTotalBudget());
    }
}
