package oop;

public interface MailStore {
    public abstract boolean sendMail(Message msg);
    public abstract Messages getMailsUser(User user);
    public abstract Messages getAllMessages();
    public abstract int getNumMessages();
}
