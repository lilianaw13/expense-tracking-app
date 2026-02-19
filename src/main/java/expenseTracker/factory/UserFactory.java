package main.java.expenseTracker.factory;
import main.java.expenseTracker.model.IUser;
public abstract class UserFactory {
    public abstract IUser createUser(String id, String name);
}
