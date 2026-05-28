package main.java.expenseTracker.UI;

import main.java.expenseTracker.AbstractFactory.DatabaseRepositoryFactory;
import main.java.expenseTracker.AbstractFactory.InMemoryRepositoryFactory;
import main.java.expenseTracker.adapter.ExternalExpense;
import main.java.expenseTracker.adapter.ExternalExpenseAdapter;
import main.java.expenseTracker.bridge.CategoryExpenseReport;
import main.java.expenseTracker.bridge.ExpenseReport;
import main.java.expenseTracker.bridge.MonthlyExpenseReport;
import main.java.expenseTracker.bridge.ReportRenderer;
import main.java.expenseTracker.composite.ExpenseGroup;
import main.java.expenseTracker.composite.ExpenseLeaf;
import main.java.expenseTracker.facade.ExpenseFacade;
import main.java.expenseTracker.factory.AdminUserFactory;
import main.java.expenseTracker.factory.RegularUserFactory;
import main.java.expenseTracker.factory.UserFactory;
import main.java.expenseTracker.singleton.AppContext;
import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.model.AdminUser;
import main.java.expenseTracker.model.Expense;
import main.java.expenseTracker.model.User;
import main.java.expenseTracker.service.ExpenseService;
import main.java.expenseTracker.builder.*;
import main.java.expenseTracker.command.*;
import main.java.expenseTracker.observer.*;
import main.java.expenseTracker.ChainOfResponsibility.*;
import main.java.expenseTracker.decorator.*;
import main.java.expenseTracker.strategy.*;
import main.java.expenseTracker.memento.*;
import main.java.expenseTracker.flyweight.CategoryFlyweightFactory;
import main.java.expenseTracker.State.BudgetAccount;
import main.java.expenseTracker.TemplateMethod.CsvExpenseReportGenerator;
import main.java.expenseTracker.TemplateMethod.ExpenseReportGenerator;
import main.java.expenseTracker.TemplateMethod.HtmlExpenseReportGenerator;
import main.java.expenseTracker.TemplateMethod.SummaryExpenseReportGenerator;
import main.java.expenseTracker.iterator.ExpenseCollection;
import main.java.expenseTracker.iterator.IExpenseIterator;
import main.java.expenseTracker.mediator.BudgetTrackerComponent;
import main.java.expenseTracker.mediator.ExpenseInputComponent;
import main.java.expenseTracker.mediator.ExpenseMediator;
import main.java.expenseTracker.mediator.NotificationComponent;
import main.java.expenseTracker.mediator.ReportComponent;
import main.java.expenseTracker.proxy.IReportService;
import main.java.expenseTracker.proxy.ReportServiceProxy;
import main.java.expenseTracker.visitor.IVisitable;
import main.java.expenseTracker.visitor.TotalAmountVisitor;
import main.java.expenseTracker.visitor.VisitableCategory;
import main.java.expenseTracker.visitor.VisitableExpense;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JavaBridge {

    private ExpenseService expenseService;
    private CommandManager commandManager;
    private ExpenseHistory expenseHistory;
    private CategoryFlyweightFactory categoryFactory;
    private BudgetAccount budgetAccount;
    private IExpenseHandler validationChain;
    private ExpenseProcessor processor;
    private ExpenseInputComponent mediatorInput;
    private User currentUser;
    private List<Expense> adminExpenses;

    public JavaBridge() {
        AppContext context = AppContext.getInstance();
        try {
            context.setRepositoryFactory(new DatabaseRepositoryFactory());
        } catch (RuntimeException e) {
            System.out.println("Database unavailable. The app will use in-memory storage.");
            System.out.println(e.getMessage());
            context.setRepositoryFactory(new InMemoryRepositoryFactory());
        }
        this.expenseService = context.getExpenseService();
        this.commandManager = new CommandManager();
        this.expenseHistory = new ExpenseHistory();
        this.categoryFactory = new CategoryFlyweightFactory();
        this.budgetAccount = new BudgetAccount(5000.0);

        UserFactory userFactory = new RegularUserFactory();
        this.currentUser = (User) userFactory.createUser("regular-1", "Liliana");
        this.adminExpenses = createAdminExpenses();

        // Observer — notificare automată
        expenseService.addObserver(new ExpenseLogObserver());
        expenseService.addObserver(new LargeExpenseObserver());

        // Chain of Responsibility — validare
        this.validationChain = new AmountValidationHandler();
        validationChain.setNext(new CategoryValidationHandler())
                .setNext(new BudgetLimitHandler(5000))
                .setNext(new FraudDetectionHandler());

        // Decorator — logging + validare
        this.processor = new LoggingDecorator(
                new ValidationDecorator(
                        expense -> {
                            IExpenseCommand command = new AddExpenseCommand(expenseService, expense);
                            commandManager.executeCommand(command);
                        }
                )
        );

        this.mediatorInput = new ExpenseInputComponent();
        new ExpenseMediator(
                mediatorInput,
                new BudgetTrackerComponent(5000.0),
                new NotificationComponent(),
                new ReportComponent()
        );

        // Strategy default
        expenseService.setSavingStrategy(new BalancedSavingStrategy());
    }

    // BUILDER + COMMAND + CHAIN + DECORATOR + OBSERVER + STATE + MEMENTO
    public String addExpense(double amount, String categoryName, String description) {
        try {
            Category category = categoryFactory.getCategory(categoryName); // Flyweight
            IBuilder builder = new SimpleExpenseBuilder();
            ExpenseDirector director = new ExpenseDirector(builder);
            Expense expense = director.makeSimple(amount, category, description); // Builder

            String validation = validationChain.handle(expense); // Chain of Responsibility
            if (!validation.contains("APPROVED")) {
                return "{\"success\":false,\"message\":\"" + validation + "\"}";
            }

            if (currentUser instanceof AdminUser) {
                adminExpenses.add(expense);
                budgetAccount.addExpense(amount);
                return "{\"success\":true,\"total\":" + getVisibleTotal() + "}";
            }

            expenseHistory.saveState(new ExpenseMemento(expenseService.getAllExpenses())); // Memento

            mediatorInput.submitExpense(expense); // Mediator
            processor.process(expense); // Decorator + Command

            budgetAccount.addExpense(amount); // State

            return "{\"success\":true,\"total\":" + expenseService.getTotalExpenses() + "}";
        } catch (Exception e) {
            return "{\"success\":false,\"message\":\"Eroare: " + e.getMessage() + "\"}";
        }
    }

    // ITERATOR + REPOSITORY
    public String getAllExpenses() {
        List<Expense> expenses = getVisibleExpenses();
        ExpenseCollection collection = new ExpenseCollection();
        for (Expense expense : expenses) {
            collection.addExpense(expense);
        }

        IExpenseIterator iterator = collection.createIterator();
        StringBuilder json = new StringBuilder("[");
        int index = 0;
        while (iterator.hasNext()) {
            Expense e = iterator.next();
            json.append("{\"index\":").append(index)
                    .append(",\"amount\":").append(e.getAmount())
                    .append(",\"category\":\"").append(escapeJson(e.getCategory().getName()))
                    .append("\",\"description\":\"").append(escapeJson(e.getDescription()))
                    .append("\"}");
            if (iterator.hasNext()) json.append(",");
            index++;
        }
        json.append("]");
        return json.toString();
    }

    // COMMAND + MEMENTO
    public String undoLast() {
        ExpenseMemento previous = expenseHistory.undo();
        if (previous != null) {
            expenseService.setAllExpenses(previous.getSavedExpenses());
            return "{\"success\":true,\"total\":" + expenseService.getTotalExpenses() + "}";
        }
        return "{\"success\":false,\"message\":\"Nimic de anulat\"}";
    }

    // STRATEGY
    public String getSavingRecommendation(double income, String strategy) {
        switch (strategy) {
            case "conservative":
                expenseService.setSavingStrategy(new ConservativeSavingStrategy()); break;
            case "aggressive":
                expenseService.setSavingStrategy(new AggressiveSavingStrategy()); break;
            default:
                expenseService.setSavingStrategy(new BalancedSavingStrategy());
        }
        return expenseService.getSavingRecommendation(income);
    }

    // PROXY
    public String getTotal() {
        return String.valueOf(getVisibleTotal());
    }

    public String signInAs(String type) {
        UserFactory factory = "admin".equals(type) ? new AdminUserFactory() : new RegularUserFactory();
        String name = "admin".equals(type) ? "Admin" : "Liliana";
        this.currentUser = (User) factory.createUser(type + "-user", name);
        return "{\"success\":true,\"role\":\"" + ("admin".equals(type) ? "admin" : "user") + "\"}";
    }

    public String startNewMonth() {
        double previousTotal = getVisibleTotal();
        if (currentUser instanceof AdminUser) {
            adminExpenses = new ArrayList<>();
        } else {
            expenseHistory.saveState(new ExpenseMemento(expenseService.getAllExpenses()));
            expenseService.setAllExpenses(new ArrayList<>());
        }
        this.budgetAccount = new BudgetAccount(5000.0);
        return "{\"success\":true,\"previousTotal\":" + previousTotal + "}";
    }

    public String generateAnnualReport(String type) {
        try {
            ReportSelection selection = selectReportGenerator(type);
            String content = selection.generator.buildReport(getVisibleExpenses());

            if ("html".equals(selection.type)) {
                content = wrapHtmlReport(content);
            }

            Path reportsDir = Path.of("reports");
            Files.createDirectories(reportsDir);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path reportPath = reportsDir.resolve("annual_report_" + timestamp + "." + selection.extension);
            Files.writeString(reportPath, content, StandardCharsets.UTF_8);

            return "{\"success\":true,\"path\":\"" + escapeJson(reportPath.toAbsolutePath().toString()) + "\"}";
        } catch (Exception e) {
            return "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}";
        }
    }

    private ReportSelection selectReportGenerator(String type) {
        String normalizedType = type == null ? "summary" : type.toLowerCase();

        switch (normalizedType) {
            case "csv":
                return new ReportSelection("csv", "csv", new CsvExpenseReportGenerator());
            case "html":
                return new ReportSelection("html", "html", new HtmlExpenseReportGenerator());
            default:
                return new ReportSelection("summary", "txt", new SummaryExpenseReportGenerator());
        }
    }

    private String wrapHtmlReport(String content) {
        return "<!DOCTYPE html>\n" +
                "<html><head><meta charset=\"UTF-8\"><title>Annual Expense Report</title>" +
                "<style>body{font-family:Arial,sans-serif;padding:24px;}table{border-collapse:collapse;width:100%;}" +
                "th,td{padding:8px;border:1px solid #ddd;text-align:left;}th{background:#f7d6e1;}</style>" +
                "</head><body><h1>Annual Expense Report</h1>" + content + "</body></html>";
    }

    private String saveAdminTextFile(String prefix, String content) {
        try {
            Path reportsDir = Path.of("reports", "admin");
            Files.createDirectories(reportsDir);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path reportPath = reportsDir.resolve(prefix + "_" + timestamp + ".txt");
            Files.writeString(reportPath, content, StandardCharsets.UTF_8);
            return reportPath.toAbsolutePath().toString();
        } catch (Exception e) {
            return "Nu s-a putut salva fisierul: " + e.getMessage();
        }
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static class ReportSelection {
        private final String type;
        private final String extension;
        private final ExpenseReportGenerator generator;

        private ReportSelection(String type, String extension, ExpenseReportGenerator generator) {
            this.type = type;
            this.extension = extension;
            this.generator = generator;
        }
    }

    public String duplicateLastExpense() {
        List<Expense> expenses = expenseService.getAllExpenses();
        if (expenses.isEmpty()) {
            return "{\"success\":false,\"message\":\"Nu exista cheltuieli de duplicat.\"}";
        }

        return duplicateExpense(expenses.size() - 1);
    }

    public String duplicateExpense(int index) {
        List<Expense> expenses = getVisibleExpenses();
        if (index < 0 || index >= expenses.size()) {
            return "{\"success\":false,\"message\":\"Cheltuiala nu exista.\"}";
        }

        Expense lastExpense = expenses.get(index);
        Expense copy = lastExpense.deepCopy();
        copy.setDescription(copy.getDescription() + " (copie)");
        if (currentUser instanceof AdminUser) {
            adminExpenses.add(copy);
            return "{\"success\":true,\"total\":" + getVisibleTotal() + "}";
        }
        return addExpense(copy.getAmount(), copy.getCategory().getName(), copy.getDescription());
    }

    public String deleteExpense(int index) {
        List<Expense> expenses = getVisibleExpenses();
        if (index < 0 || index >= expenses.size()) {
            return "{\"success\":false,\"message\":\"Cheltuiala nu exista.\"}";
        }

        if (currentUser instanceof AdminUser) {
            adminExpenses.remove(index);
            return "{\"success\":true,\"total\":" + getVisibleTotal() + "}";
        }
        expenseService.removeExpense(expenses.get(index));
        return "{\"success\":true,\"total\":" + expenseService.getTotalExpenses() + "}";
    }

    public String importExternalExpense(String title, double value) {
        try {
            ExternalExpense externalExpense = new ExternalExpense(title, value);
            ExternalExpenseAdapter adapter = new ExternalExpenseAdapter(externalExpense);

            ExpenseFacade facade = new ExpenseFacade();
            facade.addExpense(adapter);
            facade.printReport();

            return addExpense(adapter.getTotalAmount(), "Import extern", title);
        } catch (Exception e) {
            return "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}";
        }
    }

    public String cleanupDemoExpenses() {
        List<Expense> cleanedExpenses = expenseService.getAllExpenses().stream()
                .filter(expense -> !isDemoExpense(expense))
                .toList();

        int removedCount = expenseService.getAllExpenses().size() - cleanedExpenses.size();
        expenseService.setAllExpenses(cleanedExpenses);

        return "{\"success\":true,\"removed\":" + removedCount + ",\"total\":" + expenseService.getTotalExpenses() + "}";
    }

    private boolean isDemoExpense(Expense expense) {
        String description = expense.getDescription() == null ? "" : expense.getDescription();
        String categoryName = expense.getCategory() == null ? "" : expense.getCategory().getName();

        return description.contains("(copie)")
                || description.startsWith("Transfer goal ")
                || "Economii".equalsIgnoreCase(categoryName)
                || "Import extern".equalsIgnoreCase(categoryName);
    }

    public String getCompositeSummary() {
        ExpenseGroup allExpenses = new ExpenseGroup("Toate cheltuielile");
        for (Expense expense : getVisibleExpenses()) {
            allExpenses.add(new ExpenseLeaf(expense.getDescription(), expense.getAmount()));
        }
        allExpenses.print();
        return "Composite: totalul grupului de cheltuieli este " + allExpenses.getTotalAmount() + " lei.";
    }

    public String getIteratorSummary() {
        ExpenseCollection collection = new ExpenseCollection();
        for (Expense expense : getVisibleExpenses()) {
            collection.addExpense(expense);
        }

        int count = 0;
        double total = 0;
        IExpenseIterator iterator = collection.createIterator();
        while (iterator.hasNext()) {
            Expense expense = iterator.next();
            count++;
            total += expense.getAmount();
        }

        return "Iterator: am parcurs " + count + " cheltuieli, total " + total + " lei.";
    }

    public String getVisitorSummary() {
        if (!(currentUser instanceof AdminUser)) {
            return "Visitor: analiza de audit este disponibila doar pentru admin.";
        }

        List<IVisitable> visitables = new ArrayList<>();
        for (Expense expense : getVisibleExpenses()) {
            visitables.add(new VisitableExpense(expense));
            visitables.add(new VisitableCategory(expense.getCategory()));
        }

        TotalAmountVisitor visitor = new TotalAmountVisitor();
        for (IVisitable visitable : visitables) {
            visitable.accept(visitor);
        }

        String content = "Visitor audit\nTotal calculat: " + visitor.getTotal() + " lei\n"
                + "Cheltuieli analizate: " + getVisibleExpenses().size();
        String path = saveAdminTextFile("visitor_audit", content);
        return "Visitor: total calculat prin visitor = " + visitor.getTotal()
                + " lei. Salvat in: " + path;
    }

    public String getBridgeSummary(String reportType) {
        if (!(currentUser instanceof AdminUser)) {
            return "Bridge: raportul rapid intern este disponibil doar pentru admin.";
        }

        StringBuilder result = new StringBuilder();
        ReportRenderer renderer = reportContent -> result.append(reportContent);
        ExpenseReport report = "category".equals(reportType)
                ? new CategoryExpenseReport(renderer)
                : new MonthlyExpenseReport(renderer);
        report.generateReport();
        String content = "Bridge quick report\n" + result + "\nTotal curent: "
                + getVisibleTotal() + " lei";
        String path = saveAdminTextFile("bridge_quick_report", content);
        return "Bridge: " + result + " Salvat in: " + path;
    }

    public String getUserFactorySummary(String type) {
        UserFactory factory = "regular".equals(type) ? new RegularUserFactory() : new AdminUserFactory();
        String name = "regular".equals(type) ? "Liliana" : "Admin";
        User user = (User) factory.createUser(type + "-user", name);
        this.currentUser = user;
        user.showPermissions();
        String mode = "regular".equals(type)
                ? "profil personal pentru cheltuieli si goals"
                : "profil avansat pentru analize si organizare";
        return "Factory Method: utilizator activ creat ca " + user.getClass().getSimpleName()
                + " (" + mode + "). Raportul anual ramane deblocat prin Nivel 5.";
    }

    public String getAdminAuditSummary() {
        IReportService guardedReportService = new ReportServiceProxy(currentUser);
        guardedReportService.generateReport();

        if (!(currentUser instanceof AdminUser)) {
            return "Proxy: acces refuzat. Doar adminul poate vedea auditul intern.";
        }

        String content = "Proxy admin audit\nCheltuieli active: " + getVisibleExpenses().size()
                + "\nTotal: " + getVisibleTotal() + " lei"
                + "\nUser: " + currentUser.getClass().getSimpleName();
        String path = saveAdminTextFile("proxy_admin_audit", content);
        return "Proxy: acces admin permis. Audit intern: " + getVisibleExpenses().size()
                + " cheltuieli active, total " + getVisibleTotal()
                + " lei. Salvat in: " + path;
    }

    private List<Expense> getVisibleExpenses() {
        if (currentUser instanceof AdminUser) {
            return adminExpenses;
        }
        return expenseService.getAllExpenses();
    }

    private double getVisibleTotal() {
        double total = 0;
        for (Expense expense : getVisibleExpenses()) {
            total += expense.getAmount();
        }
        return total;
    }

    private List<Expense> createAdminExpenses() {
        List<Expense> expenses = new ArrayList<>();
        Category audit = categoryFactory.getCategory("Audit admin");
        Category operations = categoryFactory.getCategory("Operatiuni");
        Category reports = categoryFactory.getCategory("Rapoarte");
        expenses.add(new Expense(1290, audit, "Audit conturi utilizatori"));
        expenses.add(new Expense(840, operations, "Mentenanta baza de date"));
        expenses.add(new Expense(460, reports, "Export rapoarte lunare"));
        expenses.add(new Expense(2150, audit, "Verificare tranzactii mari"));
        return expenses;
    }

}
