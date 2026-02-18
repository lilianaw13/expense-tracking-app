package main.java.expenseTracker.model;

public class RegularUser extends User {

    public RegularUser(String id, String name) {
        super(id, name);
    }

    @Override
    public void showPermissions() {
        System.out.println("Regular user: add and view own expenses.");
    }
}
