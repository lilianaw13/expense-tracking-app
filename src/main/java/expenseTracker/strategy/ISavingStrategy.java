package main.java.expenseTracker.strategy;

public interface ISavingStrategy {
    String recommend(double income, double totalExpenses);
}