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

// Behavioral patterns (LAB 5)
import main.java.expenseTracker.strategy.*;
import main.java.expenseTracker.observer.*;
import main.java.expenseTracker.command.*;
import main.java.expenseTracker.memento.*;
import main.java.expenseTracker.iterator.*;

// Behavioral patterns (LAB 7)
import main.java.expenseTracker.ChainOfResponsibility.*;
import main.java.expenseTracker.State.*;
import main.java.expenseTracker.mediator.*;
import main.java.expenseTracker.TemplateMethod.*;
import main.java.expenseTracker.visitor.*;

import java.util.List;
import java.util.ArrayList;

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

        // =====================================================
        // BEHAVIORAL DESIGN PATTERNS (LAB 5)
        // =====================================================
        IRepositoryFactory demoFactory = new InMemoryRepositoryFactory();
        ExpenseService behavioralExpenseService = new ExpenseService(demoFactory);

        Category strategyCategory  = new Category("StrategyCategory");
        Category observerCategory  = new Category("ObserverCategory");
        Category commandCategory   = new Category("CommandCategory");

        behavioralExpenseService.saveCategory(strategyCategory);
        behavioralExpenseService.saveCategory(observerCategory);
        behavioralExpenseService.saveCategory(commandCategory);

        // =====================================================
        // STRATEGY
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

        // =====================================================
        // BEHAVIORAL DESIGN PATTERNS (LAB 7)
        // Folosim un service InMemory nou si categorii dedicate
        // =====================================================
        IRepositoryFactory lab7Factory = new InMemoryRepositoryFactory();
        ExpenseService lab7Service = new ExpenseService(lab7Factory);

        Category foodCat      = new Category("Food");
        Category transportCat = new Category("Transport");
        Category luxuryCat    = new Category("Luxury");

        lab7Service.saveCategory(foodCat);
        lab7Service.saveCategory(transportCat);
        lab7Service.saveCategory(luxuryCat);

        // =====================================================
        // CHAIN OF RESPONSIBILITY
        // Cererea trece printr-un lant de handleri de validare.
        // Fiecare handler decide daca proceseaza sau paseaza mai
        // departe. Expeditorul nu stie care handler va raspunde.
        // =====================================================
        System.out.println("\n=== CHAIN OF RESPONSIBILITY ===");

        // Construieste lantul cu inlantuire fluenta
        IExpenseHandler chain = new AmountValidationHandler();
        chain.setNext(new CategoryValidationHandler())
                .setNext(new BudgetLimitHandler(1000))
                .setNext(new FraudDetectionHandler());

        // Cerere 1: valida — trece prin tot lantul
        Expense validChainExpense = new Expense(150, foodCat, "Grocery shopping");
        System.out.println("\n>> Request 1: valid expense");
        System.out.println("   Result: " + chain.handle(validChainExpense));

        // Cerere 2: suma negativa — blocata de primul handler
        Expense negativeExpense = new Expense(-50, foodCat, "Refund attempt");
        System.out.println("\n>> Request 2: negative amount");
        System.out.println("   Result: " + chain.handle(negativeExpense));

        // Cerere 3: depaseste limita buget — blocata de BudgetLimitHandler
        Expense overBudgetExpense = new Expense(1500, transportCat, "Car rental");
        System.out.println("\n>> Request 3: over budget limit");
        System.out.println("   Result: " + chain.handle(overBudgetExpense));

        // Cerere 4: suma uriasa — flagata de FraudDetectionHandler
        Expense fraudExpense = new Expense(9999, luxuryCat, "Suspicious transfer");
        System.out.println("\n>> Request 4: potential fraud");
        System.out.println("   Result: " + chain.handle(fraudExpense));

        // =====================================================
        // STATE
        // Obiectul isi modifica comportamentul in functie de
        // starea interna. In loc de if-else gigantic pe stare,
        // fiecare stare este o clasa separata cu logica proprie.
        // =====================================================
        System.out.println("\n=== STATE ===");

        BudgetAccount budget = new BudgetAccount(1000.0);
        budget.printStatus(); // NORMAL

        budget.addExpense(400); // 40% -> ramane NORMAL
        budget.printStatus();

        budget.addExpense(350); // 75% -> trece in WARNING
        budget.printStatus();

        budget.addExpense(200); // 95% -> trece in CRITICAL
        budget.printStatus();

        budget.addExpense(100); // 105% -> trece in EXCEEDED
        budget.printStatus();

        budget.addExpense(50);  // EXCEEDED -> cheltuiala blocata
        budget.printStatus();

        // =====================================================
        // MEDIATOR
        // Componentele nu se cunosc intre ele — comunica exclusiv
        // prin mediator. Reduce dependentele de la O(n^2) la O(n).
        // =====================================================
        System.out.println("\n=== MEDIATOR ===");

        // Creeaza componentele — nu se cunosc intre ele
        ExpenseInputComponent inputComp    = new ExpenseInputComponent();
        BudgetTrackerComponent budgetComp  = new BudgetTrackerComponent(500.0);
        NotificationComponent  notifComp   = new NotificationComponent();
        ReportComponent        reportComp  = new ReportComponent();

        // Mediatorul leaga totul — el stie despre toate componentele
        new ExpenseMediator(inputComp, budgetComp, notifComp, reportComp);

        // Componentele comunica DOAR prin mediator, nu direct intre ele
        inputComp.submitExpense(new Expense(80,  foodCat,      "Lunch at restaurant"));
        inputComp.submitExpense(new Expense(150, transportCat, "Monthly bus pass"));
        inputComp.submitExpense(new Expense(300, luxuryCat,    "Designer bag"));  // Warning
        inputComp.submitExpense(new Expense(200, foodCat,      "Weekly groceries")); // Exceeded

        // =====================================================
        // TEMPLATE METHOD
        // Defineste scheletul algoritmului in clasa abstracta.
        // Subclasele suprascriu pasii specifici, nu structura.
        // Principiul Hollywood: "Nu ne suna tu, te sunam noi."
        // =====================================================
        System.out.println("\n=== TEMPLATE METHOD ===");

        // Date demo pentru rapoarte
        List<Expense> reportExpenses = new ArrayList<>();
        reportExpenses.add(new Expense(45,  foodCat,      "Coffee & snacks"));
        reportExpenses.add(new Expense(120, transportCat, "Uber rides"));
        reportExpenses.add(new Expense(850, luxuryCat,    "Smartwatch"));
        reportExpenses.add(new Expense(60,  foodCat,      "Lunch"));
        reportExpenses.add(new Expense(200, transportCat, "Train ticket"));

        // Acelasi algoritm (fetch->filter->sort->format->export), implementari diferite
        ExpenseReportGenerator csvGenerator     = new CsvExpenseReportGenerator();
        ExpenseReportGenerator htmlGenerator    = new HtmlExpenseReportGenerator();
        ExpenseReportGenerator summaryGenerator = new SummaryExpenseReportGenerator();

        csvGenerator.generateReport(reportExpenses);
        htmlGenerator.generateReport(reportExpenses);
        summaryGenerator.generateReport(reportExpenses);

        // =====================================================
        // VISITOR
        // Adauga operatii noi la obiecte fara a le modifica.
        // Double dispatch: element.accept(v) -> v.visit(element)
        // Ideal cand ierarhia e stabila si operatiile se schimba.
        // =====================================================
        System.out.println("\n=== VISITOR ===");

        // Construieste lista de elemente vizitabile
        List<IVisitable> visitables = new ArrayList<>();
        visitables.add(new VisitableExpense(new Expense(45,   foodCat,      "Coffee & snacks")));
        visitables.add(new VisitableExpense(new Expense(120,  transportCat, "Uber rides")));
        visitables.add(new VisitableExpense(new Expense(850,  luxuryCat,    "Smartwatch")));
        visitables.add(new VisitableExpense(new Expense(60,   foodCat,      "Lunch")));
        visitables.add(new VisitableCategory(foodCat));
        visitables.add(new VisitableCategory(transportCat));

        // Visitor 1: calcul total — zero modificari in Expense sau Category!
        System.out.println("--- Visitor 1: Total Amount ---");
        TotalAmountVisitor totalVisitor = new TotalAmountVisitor();
        visitables.forEach(v -> v.accept(totalVisitor));
        totalVisitor.printSummary();

        // Visitor 2: raport audit — acelasi set de obiecte, operatie complet diferita
        System.out.println("\n--- Visitor 2: Audit Report ---");
        AuditReportVisitor auditVisitor = new AuditReportVisitor();
        visitables.forEach(v -> v.accept(auditVisitor));
        auditVisitor.printReport();
    }
}