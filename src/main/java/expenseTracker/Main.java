package main.java.expenseTracker;

import main.java.expenseTracker.factory.RegularUserFactory;
import main.java.expenseTracker.factory.UserFactory;
import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.model.User;
import main.java.expenseTracker.service.ExpenseService;
import main.java.expenseTracker.builder.*;
import main.java.expenseTracker.singleton.AppContext;
import main.java.expenseTracker.AbstractFactory.FileRepositoryFactory;

public class Main {

    public static void main(String[] args) {

        // =====================================================
        //  FACTORY METHOD PATTERN
        // =====================================================

        System.out.println("=== FACTORY METHOD ===");

        UserFactory userFactory = new RegularUserFactory();
        User user = (User) userFactory.createUser("1", "Ana");
        user.showPermissions();



        // =====================================================
        // SINGLETON PATTERN
        // =====================================================

        System.out.println("\n=== SINGLETON ===");

        AppContext context = AppContext.getInstance();
        AppContext context2 = AppContext.getInstance();

        System.out.println("Singleton works: " + (context == context2));



        // =====================================================
        // ABSTRACT FACTORY PATTERN – InMemory
        // =====================================================

        System.out.println("\n=== ABSTRACT FACTORY - InMemory ===");

        ExpenseService expenseService = context.getExpenseService();

        Category food = new Category("Food");

        IBuilder simpleBuilder = new SimpleExpenseBuilder();
        ExpenseDirector director = new ExpenseDirector(simpleBuilder);

        Expense expense = director.makeSimple(50.0, food, "Lunch");

        expenseService.saveCategory(food);
        expenseService.saveExpense(expense);



        // =====================================================
        //  ABSTRACT FACTORY PATTERN – File
        // Schimbăm familia de repository
        // =====================================================

       System.out.println("\n=== ABSTRACT FACTORY - File ===");

        context.setRepositoryFactory(new FileRepositoryFactory());

        ExpenseService fileExpenseService = context.getExpenseService();

        Category rent = new Category("Rent");
        Expense rentExpense = director.makeSimple(1000.0, rent, "Apartment rent");

        fileExpenseService.saveCategory(rent);
        fileExpenseService.saveExpense(rentExpense);



        // =====================================================
        //  BUILDER PATTERN
        // Demonstrăm schimbarea ConcreteBuilder
        // =====================================================

        System.out.println("\n=== BUILDER - Change Builder ===");

        IBuilder detailedBuilder = new DetailedExpenseBuilder();
        director.changeBuilder(detailedBuilder);

        Expense detailedExpense = director.makeSimple(200.0, food, "Dinner");

        expenseService.saveExpense(detailedExpense);



        // =====================================================
        // PROTOTYPE PATTERN
        // =====================================================

        System.out.println("\n=== PROTOTYPE ===");

        Expense shallowCopy = expense.copy();
        Expense deepCopy = expense.deepCopy();

        System.out.println("Original category object: " + expense.getCategory());
        System.out.println("Shallow category object: " + shallowCopy.getCategory());
        System.out.println("Deep category object: " + deepCopy.getCategory());
    }
}