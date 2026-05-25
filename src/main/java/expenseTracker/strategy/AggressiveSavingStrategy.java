package main.java.expenseTracker.strategy;

public class AggressiveSavingStrategy implements ISavingStrategy {

    @Override
    public String recommend(double income, double totalExpenses) {
        double available = income - totalExpenses;
        if (available <= 0) {
            return String.format(
                    "Strategie agresiva: cheltuielile depasesc venitul cu %.2f lei. Nu este realist sa fortezi goals acum; intai trebuie eliberat buget disponibil.",
                    Math.abs(available)
            );
        }

        double savings = Math.max(0, available * 0.50);
        double spendingLimit = Math.max(0, available - savings);

        return String.format(
                "Strategie agresiva: dupa cheltuieli iti raman %.2f lei. Pune %.2f lei la goals si limiteaza cheltuielile flexibile la %.2f lei.",
                available,
                savings,
                spendingLimit
        );
    }
}
