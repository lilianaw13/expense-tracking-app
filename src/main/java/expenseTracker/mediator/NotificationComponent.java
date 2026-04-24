package main.java.expenseTracker.mediator;

// Componenta de notificari — primeste mesaje de la mediator, nu de la alte componente direct
public class NotificationComponent extends MediatorComponent {

    public void sendAlert(String message) {
        System.out.println("[Notification] ⚠  ALERT: " + message);
    }

    public void sendInfo(String message) {
        System.out.println("[Notification] ℹ  INFO: " + message);
    }
}