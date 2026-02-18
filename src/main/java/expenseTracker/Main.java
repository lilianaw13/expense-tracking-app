package main.java.expenseTracker;
import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.model.RegularUser;
import main.java.expenseTracker.model.User;
import main.java.expenseTracker.repository.*;
import main.java.expenseTracker.service.*;

public class Main {

    public static void main(String[] args) {

        User user = new RegularUser("1", "Anatolia");
        user.showPermissions();

        IExpenseRepository repo = new InMemoryExpenseRepository();
        ExpenseService expenseService = new ExpenseService(repo);

        Category food = new Category("Food");
        Expense expense = new Expense(50.0, food, "Lunch");

        expenseService.addExpense(expense);
    }
}
