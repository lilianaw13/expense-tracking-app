package main.java.expenseTracker.prototype;

public interface IPrototype<Expense> {

   Expense copy();      // shallow copy

    Expense deepCopy();  // deep copy
}