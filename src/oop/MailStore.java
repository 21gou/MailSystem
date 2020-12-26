package oop;

import java.util.ArrayList;

public interface MailStore {
    /**
     * Send mail to the receiver user
     * @param msg new message
     */
    void sendMail(Message msg);

    /**
     * Get mails from sended to user
     * @param user
     * @return
     */
    ArrayList<Message> getMailsUser(User user);

    /**
     * Get all the messages from the store, only used by MailSystem,
     * avoid inefficient code
     * @return
     */
    ArrayList<Message> getAllMessages();

    /**
     * Get number of messages avaible in the store, only used by MailSystem,
     * avoid inefficient code
     * @return
     */
    long getNumMessages();
}
