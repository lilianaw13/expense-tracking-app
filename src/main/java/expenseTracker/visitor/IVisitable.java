package main.java.expenseTracker.visitor;

// Interfata Element — declara metoda accept() pentru fiecare obiect vizitabil
public interface IVisitable {
    void accept(IExpenseVisitor visitor);
}