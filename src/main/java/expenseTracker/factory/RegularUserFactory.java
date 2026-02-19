package main.java.expenseTracker.factory;

import main.java.expenseTracker.model.RegularUser;
import main.java.expenseTracker.model.IUser;

public class RegularUserFactory extends UserFactory {

    @Override
    public IUser createUser(String id, String name) {
        return new RegularUser(id, name);
    }
}
