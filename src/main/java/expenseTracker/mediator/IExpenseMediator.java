package main.java.expenseTracker.mediator;

// Interfata Mediator — centralizeaza comunicarea intre componente
public interface IExpenseMediator {
    void notify(MediatorComponent sender, String event, Object data);
}