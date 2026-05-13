package main.java.expenseTracker.UI;

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

}