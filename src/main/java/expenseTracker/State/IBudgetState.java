package main.java.expenseTracker.State;

// Interfata stare — declara operatiile posibile pe buget
public interface IBudgetState {
    void addExpense(BudgetAccount account, double amount);
    void printStatus(BudgetAccount account);
    String getStateName();
}
