package main.java.expenseTracker.model;

public class SpecialExpense extends Expense {

    private String extraNote;

    public SpecialExpense(double amount, Category category, String description, String extraNote) {
        super(amount, category, description);
        this.extraNote = extraNote;
    }

    // Constructor de copiere (important pentru UML)
    public SpecialExpense(SpecialExpense prototype) {
        super(prototype.getAmount(), prototype.getCategory(), prototype.getDescription());
        this.extraNote = prototype.extraNote;
    }

    @Override
    public SpecialExpense copy() {
        return new SpecialExpense(this);
    }

    public String getExtraNote() {
        return extraNote;
    }
}