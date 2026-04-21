package main.java.expenseTracker.strategy;

public class AggressiveSavingStrategy implements ISavingStrategy {

    @Override
    public String recommend(double income, double totalExpenses) {
        double savings = income * 0.30;
        return "Aggressive strategy: save around " + savings +
                ". Remaining after expenses: " + (income - totalExpenses);
    }
}