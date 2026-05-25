package main.java.expenseTracker.UI;

import main.java.expenseTracker.AbstractFactory.DatabaseRepositoryFactory;
import main.java.expenseTracker.AbstractFactory.InMemoryRepositoryFactory;
import main.java.expenseTracker.singleton.AppContext;
import main.java.expenseTracker.model.Category;
import main.java.expenseTracker.model.Expense;
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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JavaBridge {

    private ExpenseService expenseService;
    private CommandManager commandManager;
    private ExpenseHistory expenseHistory;
    private CategoryFlyweightFactory categoryFactory;
    private BudgetAccount budgetAccount;
    private IExpenseHandler validationChain;
    private ExpenseProcessor processor;

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
                        new BasicExpenseProcessor(expenseService)
                )
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

            expenseHistory.saveState(new ExpenseMemento(expenseService.getAllExpenses())); // Memento

            IExpenseCommand command = new AddExpenseCommand(expenseService, expense); // Command
            commandManager.executeCommand(command);

            budgetAccount.addExpense(amount); // State

            return "{\"success\":true,\"total\":" + expenseService.getTotalExpenses() + "}";
        } catch (Exception e) {
            return "{\"success\":false,\"message\":\"Eroare: " + e.getMessage() + "\"}";
        }
    }

    // ITERATOR + REPOSITORY
    public String getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            json.append("{\"amount\":").append(e.getAmount())
                    .append(",\"category\":\"").append(e.getCategory().getName())
                    .append("\",\"description\":\"").append(e.getDescription())
                    .append("\"}");
            if (i < expenses.size() - 1) json.append(",");
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
        return String.valueOf(expenseService.getTotalExpenses());
    }

    public String generateAnnualReport(String type) {
        try {
            ReportSelection selection = selectReportGenerator(type);
            String content = selection.generator.buildReport(expenseService.getAllExpenses());

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

}
