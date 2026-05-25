package main.java.expenseTracker.strategy;

public class ConservativeSavingStrategy implements ISavingStrategy {

    @Override
    public String recommend(double income, double totalExpenses) {
        double available = income - totalExpenses;
        if (available <= 0) {
            return String.format(
                    "Strategie conservatoare: cheltuielile depasesc venitul cu %.2f lei. Pastreaza goals pe pauza si concentreaza-te pe reducerea cheltuielilor obligatorii.",
                    Math.abs(available)
            );
        }

        double savings = Math.max(0, available * 0.15);
        double spendingLimit = Math.max(0, available - savings);

        return String.format(
                "Strategie conservatoare: dupa cheltuieli iti raman %.2f lei. Pune %.2f lei la goals si pastreaza %.2f lei pentru siguranta si cheltuieli flexibile.",
                available,
                savings,
                spendingLimit
        );
    }
}
