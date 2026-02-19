package main.java.expenseTracker.factory;

import main.java.expenseTracker.model.AdminUser;
import main.java.expenseTracker.model.IUser;

public class AdminUserFactory extends UserFactory {

    @Override
    public IUser createUser(String id, String name) {
        return new AdminUser(id, name);
    }
}
