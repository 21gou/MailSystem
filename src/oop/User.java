package oop;

public class User {
    private String username, name;
    private int yearBirth;

    // Maybe private with getCopy method?
    public MailBox mailbox;

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
}