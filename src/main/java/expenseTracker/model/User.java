package main.java.expenseTracker.model;

public abstract class User implements IUser {
    private String id;
    private String name;

    protected User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
