package main.java.expenseTracker.strategy;

public class ConservativeSavingStrategy implements ISavingStrategy {

    @Override
    public String recommend(double income, double totalExpenses) {
        double savings = income * 0.10;
        return "Conservative strategy: save at least " + savings +
                ". Remaining after expenses: " + (income - totalExpenses);
    }
}