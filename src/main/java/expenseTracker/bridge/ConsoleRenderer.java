package main.java.expenseTracker.bridge;

public class ConsoleRenderer implements ReportRenderer {

    @Override
    public void render(String reportContent) {
        System.out.println("Console Report: " + reportContent);
    }
}