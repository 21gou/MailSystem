package oop;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MailBox implements Iterable<Message> {
    private ArrayList<Message> messages;
    private MailStore store;

    public MailBox(MailStore store) {
        this.messages = new ArrayList<Message>();
        this.store = store;
    }

    public MailBox(ArrayList<Message> messages) {
        this.messages = messages;
    }

    /**
     * Retrieve all the messages addressed to the user
     * @param user target
     */
    public void updateMail(User user) {
        this.messages = this.store.getMailsUser(user);
    }

    /**
     * Return all the mails from the mailbox
     * @return mails received
     */
    public ArrayList<Message> listMail() {
        return this.messages;
    }

    /**
     * Send new message
     * @param message
     */
    public void sendMail(Message message) {
        this.store.sendMail(message);
    }

    /**
     * Sort mail with certain pattern
     * @param comparator
     * @return messages sorted
     */
    public ArrayList<Message> sortMail(Comparator<? super Message> comparator) {
        return this.messages.stream()
                .sorted(comparator)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Return messages that meet certain filter
     * @param predicate
     * @return messages filtered
     */
    public ArrayList<Message> filterUserMail(Predicate<? super Message> predicate) {
        return this.messages.stream()
                .filter(predicate)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Set different storage
     * @param store
     */
    public void setStore(MailStore store) {
        this.store = store;
    }

    @Override
    public Iterator<Message> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super Message> action) {
        this.messages.forEach(action);
    }
}
