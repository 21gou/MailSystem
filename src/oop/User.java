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

    public boolean equals(User user) {
        boolean equal = true;

        equal &= this.username.equals(user.getUsername());
        equal &= this.name.equals(user.getName());
        equal &= this.yearBirth == user.getYearBirth();

        return equal;
    }
}