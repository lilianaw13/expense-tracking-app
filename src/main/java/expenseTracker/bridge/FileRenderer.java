package main.java.expenseTracker.bridge;

public class FileRenderer implements ReportRenderer {

    @Override
    public void render(String reportContent) {
        System.out.println("File Report: " + reportContent);
    }
}