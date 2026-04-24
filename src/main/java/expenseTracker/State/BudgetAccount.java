package main.java.expenseTracker.State;

// Context — stocheaza referinta la starea curenta si deleaga operatiile catre ea
public class BudgetAccount {

    private final double totalBudget;
    private double spentAmount;
    private IBudgetState state;

    public BudgetAccount(double totalBudget) {
        this.totalBudget = totalBudget;
        this.spentAmount = 0;
        this.state = new NormalState(); // starea initiala
    }

    public void transitionTo(IBudgetState newState) {
        System.out.println("[BudgetAccount] Transition: "
                + state.getStateName() + " -> " + newState.getStateName());
        this.state = newState;
    }

    // Deleaga operatia catre starea curenta
    public void addExpense(double amount) {
        state.addExpense(this, amount);
    }

    public void printStatus() {
        state.printStatus(this);
    }

    // Getters / Setters folosite de stari
    public double getTotalBudget() { return totalBudget; }
    public double getSpentAmount() { return spentAmount; }
    public void setSpentAmount(double amount) { this.spentAmount = amount; }
    public IBudgetState getState() { return state; }

    public double getUsagePercent() {
        return (spentAmount / totalBudget) * 100.0;
    }
}
