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
import main.java.expenseTracker.AbstractFactory.IRepositoryFactory;
import main.java.expenseTracker.AbstractFactory.InMemoryRepositoryFactory;
import main.java.expenseTracker.composite.*;
import main.java.expenseTracker.facade.*;
import main.java.expenseTracker.adapter.*;
import main.java.expenseTracker.flyweight.CategoryFlyweightFactory;
import main.java.expenseTracker.decorator.*;
import main.java.expenseTracker.bridge.*;
import main.java.expenseTracker.proxy.*;
import main.java.expenseTracker.model.AdminUser;

// Behavioral patterns
import main.java.expenseTracker.strategy.*;
import main.java.expenseTracker.observer.*;
import main.java.expenseTracker.command.*;
import main.java.expenseTracker.memento.*;
import main.java.expenseTracker.iterator.*;

public class Main {

    public static void main(String[] args) {

        // =====================================================
        // FACTORY METHOD
        // Acest pattern creeaza obiecte printr-o fabrica,
        // fara sa instantiem direct clasa concreta in client.
        // =====================================================
        System.out.println("=== FACTORY METHOD ===");

        UserFactory userFactory = new RegularUserFactory();
        User user = (User) userFactory.createUser("1", "Ana");
        user.showPermissions();

        // =====================================================
        // SINGLETON
        // Acest pattern asigura existenta unei singure instante
        // a clasei AppContext in toata aplicatia.
        // =====================================================
        System.out.println("\n=== SINGLETON ===");

        AppContext context = AppContext.getInstance();
        AppContext context2 = AppContext.getInstance();

        System.out.println("Singleton works: " + (context == context2));

        // =====================================================
        // ABSTRACT FACTORY - InMemory
        // Acest pattern permite crearea unei familii de obiecte
        // compatibile, in cazul nostru repository-uri InMemory.
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
        // Aici schimbam familia de repository-uri cu una bazata
        // pe fisiere, fara sa schimbam logica din service.
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
        // Acest pattern construieste obiecte pas cu pas,
        // separand procesul de construire de obiectul final.
        // =====================================================
        System.out.println("\n=== BUILDER ===");

        IBuilder detailedBuilder = new DetailedExpenseBuilder();
        director.changeBuilder(detailedBuilder);

        Expense detailedExpense = director.makeSimple(200.0, foodCategory, "Dinner");

        expenseService.saveExpense(detailedExpense);

        // =====================================================
        // PROTOTYPE
        // Acest pattern creeaza obiecte noi prin copierea
        // unui obiect existent.
        // =====================================================
        System.out.println("\n=== PROTOTYPE ===");

        Expense shallowCopy = expense.copy();
        Expense deepCopy = expense.deepCopy();

        System.out.println("Original category: " + expense.getCategory());
        System.out.println("Shallow category: " + shallowCopy.getCategory());
        System.out.println("Deep category: " + deepCopy.getCategory());

        // =====================================================
        // FACADE + ADAPTER
        // Facade simplifica lucrul cu mai multe clase,
        // iar Adapter transforma o clasa incompatibila
        // intr-una compatibila cu sistemul nostru.
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
        // Acest pattern reutilizeaza obiecte comune pentru a
        // economisi memorie.
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
        // Acest pattern adauga functionalitati suplimentare
        // unui obiect fara sa modificam clasa lui de baza.
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
        // Acest pattern separa abstractia de implementare,
        // astfel incat ambele sa poata varia independent.
        // =====================================================
        System.out.println("\n=== BRIDGE ===");

        ExpenseReport monthlyConsoleReport = new MonthlyExpenseReport(new ConsoleRenderer());
        monthlyConsoleReport.generateReport();

        ExpenseReport categoryFileReport = new CategoryExpenseReport(new FileRenderer());
        categoryFileReport.generateReport();

        // =====================================================
        // PROXY
        // Acest pattern controleaza accesul la un obiect.
        // In cazul nostru, verificam drepturile utilizatorului
        // inainte de generarea raportului.
        // =====================================================
        System.out.println("\n=== PROXY ===");

        User admin = new AdminUser("99", "Maria");
        IReportService proxyReport = new ReportServiceProxy(admin);
        proxyReport.generateReport();

        User regular = (User) userFactory.createUser("2", "Ion");
        IReportService deniedProxyReport = new ReportServiceProxy(regular);
        deniedProxyReport.generateReport();

        // =====================================================
        // BEHAVIORAL DESIGN PATTERNS
        // Pentru aceste exemple folosim din nou repository-uri
        // InMemory, ca demonstratia sa fie simpla si clara.
        // =====================================================
        IRepositoryFactory demoFactory = new InMemoryRepositoryFactory();
        ExpenseService behavioralExpenseService = new ExpenseService(demoFactory);

        Category strategyCategory = new Category("StrategyCategory");
        Category observerCategory = new Category("ObserverCategory");
        Category commandCategory = new Category("CommandCategory");

        behavioralExpenseService.saveCategory(strategyCategory);
        behavioralExpenseService.saveCategory(observerCategory);
        behavioralExpenseService.saveCategory(commandCategory);

        // =====================================================
        // STRATEGY
        // Acest pattern permite schimbarea algoritmului in mod
        // dinamic. Aici schimbam strategia de economisire
        // fara sa modificam codul din ExpenseService.
        // =====================================================
        System.out.println("\n=== STRATEGY ===");

        behavioralExpenseService.setSavingStrategy(new ConservativeSavingStrategy());
        System.out.println(behavioralExpenseService.getSavingRecommendation(5000));

        behavioralExpenseService.setSavingStrategy(new BalancedSavingStrategy());
        System.out.println(behavioralExpenseService.getSavingRecommendation(5000));

        behavioralExpenseService.setSavingStrategy(new AggressiveSavingStrategy());
        System.out.println(behavioralExpenseService.getSavingRecommendation(5000));

        // =====================================================
        // OBSERVER
        // Acest pattern notifica automat mai multi observatori
        // atunci cand apare o schimbare. Aici, cand se adauga
        // o cheltuiala, observatorii reactioneaza automat.
        // =====================================================
        System.out.println("\n=== OBSERVER ===");

        IExpenseObserver logObserver = new ExpenseLogObserver();
        IExpenseObserver largeExpenseObserver = new LargeExpenseObserver();

        behavioralExpenseService.addObserver(logObserver);
        behavioralExpenseService.addObserver(largeExpenseObserver);

        Expense observedExpense1 = new Expense(150, observerCategory, "Books");
        Expense observedExpense2 = new Expense(2000, observerCategory, "Laptop");

        behavioralExpenseService.saveExpense(observedExpense1);
        behavioralExpenseService.saveExpense(observedExpense2);

        // =====================================================
        // COMMAND
        // Acest pattern transforma o actiune intr-un obiect.
        // Aici adaugarea unei cheltuieli este o comanda care
        // poate fi executata si apoi anulata prin undo.
        // =====================================================
        System.out.println("\n=== COMMAND ===");

        CommandManager commandManager = new CommandManager();

        Expense commandExpense = new Expense(300, commandCategory, "Shoes");
        IExpenseCommand addExpenseCommand =
                new AddExpenseCommand(behavioralExpenseService, commandExpense);

        System.out.println("Before command execute: " + behavioralExpenseService.getAllExpenses().size());
        commandManager.executeCommand(addExpenseCommand);
        System.out.println("After command execute: " + behavioralExpenseService.getAllExpenses().size());

        commandManager.undoLastCommand();
        System.out.println("After undo command: " + behavioralExpenseService.getAllExpenses().size());

        // =====================================================
        // MEMENTO
        // Acest pattern salveaza starea unui obiect pentru a
        // putea reveni la ea mai tarziu. Aici salvam lista de
        // cheltuieli si restauram starea anterioara.
        // =====================================================
        System.out.println("\n=== MEMENTO ===");

        ExpenseHistory history = new ExpenseHistory();

        history.saveState(new ExpenseMemento(behavioralExpenseService.getAllExpenses()));
        System.out.println("State saved.");

        Expense wrongExpense = new Expense(9999, commandCategory, "Wrong expense");
        behavioralExpenseService.saveExpense(wrongExpense);

        System.out.println("Expenses after wrong change: " + behavioralExpenseService.getAllExpenses().size());

        ExpenseMemento previousState = history.undo();
        if (previousState != null) {
            behavioralExpenseService.setAllExpenses(previousState.getSavedExpenses());
            System.out.println("Previous state restored.");
        }

        System.out.println("Expenses after restore: " + behavioralExpenseService.getAllExpenses().size());

        // =====================================================
        // ITERATOR
        // Acest pattern permite parcurgerea unei colectii fara
        // a expune structura interna a acesteia. Aici parcurgem
        // cheltuielile printr-un iterator custom.
        // =====================================================
        System.out.println("\n=== ITERATOR ===");

        Expense iteratorExpense1 = new Expense(100, strategyCategory, "Notebook");
        Expense iteratorExpense2 = new Expense(250, strategyCategory, "Headphones");
        Expense iteratorExpense3 = new Expense(400, strategyCategory, "Keyboard");

        behavioralExpenseService.saveExpense(iteratorExpense1);
        behavioralExpenseService.saveExpense(iteratorExpense2);
        behavioralExpenseService.saveExpense(iteratorExpense3);

        ExpenseCollection collection = new ExpenseCollection();

        for (Expense currentExpense : behavioralExpenseService.getAllExpenses()) {
            collection.addExpense(currentExpense);
        }

        IExpenseIterator iterator = collection.createIterator();

        while (iterator.hasNext()) {
            Expense currentExpense = iterator.next();
            System.out.println(
                    currentExpense.getAmount() + " | " +
                            currentExpense.getCategory().getName() + " | " +
                            currentExpense.getDescription()
            );
        }
    }
}