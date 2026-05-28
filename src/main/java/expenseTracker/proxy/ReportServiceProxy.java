package main.java.expenseTracker.proxy;

import main.java.expenseTracker.model.AdminUser;
import main.java.expenseTracker.model.User;

public class ReportServiceProxy implements IReportService {
    private RealReportService realReportService;
    private User user;

    public ReportServiceProxy(User user) {
        this.user = user;
        this.realReportService = new RealReportService();
    }

    @Override
    public void generateReport() {
        if (user instanceof AdminUser) {
            System.out.println("Admin access granted for: " + user.getName());
            realReportService.generateReport();
        } else {
            System.out.println("Access denied. Admin profile is required.");
        }
    }
}
