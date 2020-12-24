package oop;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return yearBirth == user.getYearBirth()
                && Objects.equals(username, user.getUsername())
                && Objects.equals(name, user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, yearBirth);
    }
}