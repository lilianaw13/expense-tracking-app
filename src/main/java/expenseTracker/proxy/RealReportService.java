package main.java.expenseTracker.proxy;

public class RealReportService implements IReportService {

    @Override
    public void generateReport() {
        System.out.println("Real report service: generating expense report...");
    }
}