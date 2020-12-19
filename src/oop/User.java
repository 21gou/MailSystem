package oop;

public class User {
    private String username, name;
    private int yearBirth;

    // Maybe private with getCopy method?
    private MailBox mailbox;

    public User(String username, String name, int yearBirth) {
        this.username = username;
        this.name = name;

        this.yearBirth = yearBirth;
        this.mailbox = new MailBox();
    }

    public User(String username, String name, int yearBirth, MailBox mailbox) {
        this.username = username;
        this.name = name;

        this.yearBirth = yearBirth;
        this.mailbox = mailbox;
    }

    public void getMailBox() {

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