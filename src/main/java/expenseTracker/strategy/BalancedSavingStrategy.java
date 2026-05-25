package main.java.expenseTracker.strategy;

public class BalancedSavingStrategy implements ISavingStrategy {

    @Override
    public String recommend(double income, double totalExpenses) {
        double available = income - totalExpenses;
        if (available <= 0) {
            return String.format(
                    "Strategie echilibrata: cheltuielile depasesc venitul cu %.2f lei. Nu transfera bani la goals acum; redu cheltuielile flexibile sau mareste venitul disponibil.",
                    Math.abs(available)
            );
        }

        double savings = Math.max(0, available * 0.30);
        double spendingLimit = Math.max(0, available - savings);

        return String.format(
                "Strategie echilibrata: dupa cheltuieli iti raman %.2f lei. Pune %.2f lei la goals si pastreaza %.2f lei pentru cheltuieli flexibile.",
                available,
                savings,
                spendingLimit
        );
    }
}
