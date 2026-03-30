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
import main.java.expenseTracker.flyweight.CategoryFlyweightFactory;
import main.java.expenseTracker.decorator.*;
import main.java.expenseTracker.bridge.*;
import main.java.expenseTracker.proxy.*;
import main.java.expenseTracker.model.AdminUser;

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

        // =====================================================
// FLYWEIGHT
// =====================================================
        System.out.println("\n=== FLYWEIGHT ===");

        CategoryFlyweightFactory categoryFactory = new CategoryFlyweightFactory();

        Category cat1 = categoryFactory.getCategory("Food");
        Category cat2 = categoryFactory.getCategory("Food");
        Category cat3 = categoryFactory.getCategory("Transport");

        System.out.println("cat1 == cat2: " + (cat1 == cat2));
        System.out.println("Total unique categories: " + categoryFactory.getCategoryCount());

// =====================================================
// DECORATOR
// =====================================================
        System.out.println("\n=== DECORATOR ===");

        ExpenseProcessor processor = new LoggingDecorator(
                new ValidationDecorator(
                        new BasicExpenseProcessor(expenseService)
                )
        );

        Expense validExpense = new Expense(150.0, cat1, "Groceries");
        processor.process(validExpense);

        Expense invalidExpense = new Expense(-20.0, cat1, "Invalid expense");
        processor.process(invalidExpense);

// =====================================================
// BRIDGE
// =====================================================
        System.out.println("\n=== BRIDGE ===");

        ExpenseReport monthlyConsoleReport = new MonthlyExpenseReport(new ConsoleRenderer());
        monthlyConsoleReport.generateReport();

        ExpenseReport categoryFileReport = new CategoryExpenseReport(new FileRenderer());
        categoryFileReport.generateReport();

// =====================================================
// PROXY
// =====================================================
        System.out.println("\n=== PROXY ===");

        User admin = new AdminUser("99", "Maria");
        IReportService proxyReport = new ReportServiceProxy(admin);
        proxyReport.generateReport();

        User regular = (User) userFactory.createUser("2", "Ion");
        IReportService deniedProxyReport = new ReportServiceProxy(regular);
        deniedProxyReport.generateReport();

    }
}
