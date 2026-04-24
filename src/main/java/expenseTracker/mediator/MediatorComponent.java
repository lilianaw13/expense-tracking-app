package main.java.expenseTracker.mediator;

// Clasa de baza pentru toate componentele — stocheaza referinta la mediator
public abstract class MediatorComponent {
    protected IExpenseMediator mediator;

    public void setMediator(IExpenseMediator mediator) {
        this.mediator = mediator;
    }
}