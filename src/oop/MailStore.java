package oop;

public interface MailStore {
    public abstract boolean sendMail(Message msg);
    public abstract Messages getMails(User user);
}
