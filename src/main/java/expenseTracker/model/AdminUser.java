package main.java.expenseTracker.model;

public class AdminUser extends User {

    public AdminUser(String id, String name) {
        super(id, name);
    }

    @Override
    public void showPermissions() {
        System.out.println("Admin user: manage categories and view reports.");
    }
}
