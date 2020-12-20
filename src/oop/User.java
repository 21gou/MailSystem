package oop;

public class User {
    private String username, name;
    private int yearBirth;


    public User(String username, String name, int yearBirth) {
        this.username = username;
        this.name = name;

        this.yearBirth = yearBirth;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public int getYearBirth() {
        return this.yearBirth;
    }
}