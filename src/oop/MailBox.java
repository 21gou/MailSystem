package oop;

import patterns.ObservableMailBox;
import patterns.ObserverFilter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MailBox extends ObservableMailBox implements Iterable<Message> {
    private User user;
    private MailStore store;
    private ArrayList<ObserverFilter> filters;

    private ArrayList<Message> spam;
    private ArrayList<Message> messages;

    public MailBox(User user, MailStore store) {
        this.user = user;
        this.store = store;
        this.filters = new ArrayList<ObserverFilter>();

        this.spam = new ArrayList<Message>();
        this.messages = new ArrayList<Message>();
    }

    /**
     * Retrieve all the messages addressed to the user
     */
    public void updateMail() {
        // Clean spam list for avoid repeated messages
        spam = new ArrayList<Message>();

        // Create copy in order to avoid modification of store
        messages = store.getMailsUser(user).stream()
                .map(msg -> msg.clone())
                .collect(Collectors.toCollection(ArrayList::new));

        notifyObserver();
    }

    /**
     * Return all the mails from the mailbox
     * @return mails received
     */
    public ArrayList<Message> listMail() {
        return messages;
    }

    /**
     * Return all the mails from spam list
     * @return
     */
    public ArrayList<Message> listSpam() {
        return spam;
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
     * Set new user and update in order to get new messages
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
        this.updateMail();
    }

    /**
     * Set different storage
     * @param store
     */
    public void setStore(MailStore store) {
        this.store = store;
    }

    public void attach(ObserverFilter filter) {
        this.filters.add(filter);
    }


    public void detach(ObserverFilter filter) {
        filters.remove(filter);
    }

    public void notifyObserver() {
        for(ObserverFilter filter: filters) {
            filter.update();
        }
    }

    public void setMessageSpam(Message message) {
        this.messages.remove(message);
        this.spam.add(message);
    }

    @Override
    public Iterator<Message> iterator() {
        return this.messages.iterator();
    }

    @Override
    public void forEach(Consumer<? super Message> action) {
        this.messages.forEach(action);
    }
}
