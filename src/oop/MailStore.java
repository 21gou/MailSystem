package oop;

public interface MailStore {
    /**
     * Send mail to the receiver user
     * @param msg new message
     */
    public abstract void sendMail(Message msg);

    /**
     * Get mails from sended to user
     * @param user
     * @return
     */
    public abstract Messages getMailsUser(User user);

    /**
     * Get all the messages from the store
     * @return
     */
    public abstract Messages getAllMessages();

    /**
     * Get number of messages avaible in the store
     * @return
     */
    public abstract int getNumMessages();
}
