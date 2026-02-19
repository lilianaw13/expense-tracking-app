package main.java.expenseTracker;
import main.java.expenseTracker.AbstractFactory.IRepositoryFactory;
import main.java.expenseTracker.AbstractFactory.InMemoryRepositoryFactory;
import main.java.expenseTracker.factory.RegularUserFactory;
import main.java.expenseTracker.factory.UserFactory;
import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.model.RegularUser;
import main.java.expenseTracker.model.User;
import main.java.expenseTracker.repository.*;
import main.java.expenseTracker.service.*;

public class Main {

    public static void main(String[] args) {

// factory method
        UserFactory userFactory = new RegularUserFactory();
        User user = (User) userFactory.createUser("1", "Ana");
        user.showPermissions();

        // abstract factory
// familia InMemory
        IRepositoryFactory repositoryFactory = new InMemoryRepositoryFactory();
        ExpenseService expenseService = new ExpenseService(repositoryFactory);

        Category food = new Category("Food");
        Expense expense = new Expense(50.0, food, "Lunch");

        expenseService.saveCategory(food);
        expenseService.saveExpense(expense);

// familia File
    //    IRepositoryFactory factory = new FileRepositoryFactory();

      //  ExpenseService service = new ExpenseService(factory);

       // Category food = new Category("Food");
      //  Expense expense = new Expense(100, food, "Lunch");

      //  service.saveCategory(food);
      //  service.saveExpense(expense);


    }
}
