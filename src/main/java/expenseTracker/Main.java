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
import main.java.expenseTracker.composite.*;
import main.java.expenseTracker.facade.*;
import main.java.expenseTracker.adapter.*;
public class Main {

    public static void main(String[] args) {

        // =====================================================
        // FACTORY METHOD
        // =====================================================
        System.out.println("=== FACTORY METHOD ===");

        UserFactory userFactory = new RegularUserFactory();
        User user = (User) userFactory.createUser("1", "Ana");
        user.showPermissions();

        // =====================================================
        // SINGLETON
        // =====================================================
        System.out.println("\n=== SINGLETON ===");

        AppContext context = AppContext.getInstance();
        AppContext context2 = AppContext.getInstance();

        System.out.println("Singleton works: " + (context == context2));

        // =====================================================
        // ABSTRACT FACTORY - InMemory
        // =====================================================
        System.out.println("\n=== ABSTRACT FACTORY - InMemory ===");

        ExpenseService expenseService = context.getExpenseService();

        Category foodCategory = new Category("Food");

        IBuilder simpleBuilder = new SimpleExpenseBuilder();
        ExpenseDirector director = new ExpenseDirector(simpleBuilder);

        Expense expense = director.makeSimple(50.0, foodCategory, "Lunch");

        expenseService.saveCategory(foodCategory);
        expenseService.saveExpense(expense);

        // =====================================================
        // ABSTRACT FACTORY - File
        // =====================================================
        System.out.println("\n=== ABSTRACT FACTORY - File ===");

        context.setRepositoryFactory(new FileRepositoryFactory());

        ExpenseService fileExpenseService = context.getExpenseService();

        Category rent = new Category("Rent");
        Expense rentExpense = director.makeSimple(1000.0, rent, "Apartment rent");

        fileExpenseService.saveCategory(rent);
        fileExpenseService.saveExpense(rentExpense);

        // =====================================================
        // BUILDER
        // =====================================================
        System.out.println("\n=== BUILDER ===");

        IBuilder detailedBuilder = new DetailedExpenseBuilder();
        director.changeBuilder(detailedBuilder);

        Expense detailedExpense = director.makeSimple(200.0, foodCategory, "Dinner");

        expenseService.saveExpense(detailedExpense);

        // =====================================================
        // PROTOTYPE
        // =====================================================
        System.out.println("\n=== PROTOTYPE ===");

        Expense shallowCopy = expense.copy();
        Expense deepCopy = expense.deepCopy();

        System.out.println("Original category: " + expense.getCategory());
        System.out.println("Shallow category: " + shallowCopy.getCategory());
        System.out.println("Deep category: " + deepCopy.getCategory());

        // =====================================================
        // FACADE + ADAPTER
        // =====================================================
        System.out.println("\n=== FACADE + ADAPTER ===");

        ExpenseFacade facade = new ExpenseFacade();

        ExpenseLeaf taxi = new ExpenseLeaf("Taxi", 20);
        ExpenseLeaf food = new ExpenseLeaf("Food", 30);

        facade.addExpense(taxi);
        facade.addExpense(food);

        facade.printReport();

        ExternalExpense bankExpense = new ExternalExpense("Bank Payment", 50);

        ExternalExpenseAdapter adaptedExpense =
                new ExternalExpenseAdapter(bankExpense);

        facade.addExpense(adaptedExpense);

        facade.printReport();
    }
}
