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
            System.out.println("Access granted for admin: " + user.getName());
            realReportService.generateReport();
        } else {
            System.out.println("Access denied. Only admin can generate reports.");
        }
    }
}