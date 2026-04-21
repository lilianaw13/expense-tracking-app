package main.java.expenseTracker.strategy;

public class BalancedSavingStrategy implements ISavingStrategy {

    @Override
    public String recommend(double income, double totalExpenses) {
        double savings = income * 0.20;
        return "Balanced strategy: save around " + savings +
                ". Remaining after expenses: " + (income - totalExpenses);
    }
}