package oop;

import reflection.Config;
import reflection.DynamicProxy;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Config(store="store.MemMailStore", log=true)
public class MailSystem {
    private HashMap<String, User> accounts;
    private HashMap<String, MailBox> mailboxes;

    private MailStore store;

    public MailSystem() {
        loadAnotation();

        this.accounts = new HashMap<String, User>();
        this.mailboxes = new HashMap<String, MailBox>();
    }

    /**
     * Add user to the system and retrieves the mailbox assigned,
     * will return null if user is already in the system
     * @param user
     * @return
     */
    public MailBox addUser(User user) {
        if(!accounts.containsKey(user.getUsername())) {
            accounts.put(user.getUsername(), user);
            mailboxes.put(user.getUsername(), new MailBox(user, store));

            return mailboxes.get(user.getUsername());
        } else {
            return null;
        }
    }

    /**
     * Return mailbox of user providing the username by parameter
     * @param username
     * @return
     */
    public MailBox logIn(String username) {
        if(mailboxes.containsKey(username)) {
            return mailboxes.get(username);
        } else {
            return null;
        }
    }

    /**
     *
     * @return
     */
    public ArrayList<Message> getMessages() {
        return store.getAllMessages();
    }

    /**
     * Get all the users that had signed up in the system
     * @return
     */
    public ArrayList<User> getUsers() {
        ArrayList<User> users = accounts.entrySet().stream()
                .map(entry -> entry.getValue())
                .collect(Collectors.toCollection(ArrayList::new));

        return users;
    }

    /**
     * Returns certain messages filtered by the predicate passed by parameter
     * @param predicate
     * @return
     */
    public ArrayList<Message> filterMsgs(Predicate<? super Message> predicate) {
        return this.getMessages().stream()
                .filter((Predicate<? super Message>) predicate)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get number of messages in the system
     * @return
     */
    public long countTotalMsgs() {
        return store.getNumMessages();
    }

    /**
     * Get average of messages per user
     * @return
     */
    public long getAverageUserMsgs() {
        return store.getNumMessages()/(long)accounts.size();
    }

    /**
     * Return map of messages grouped by equal subject
     * @return grouped messages
     */
    public Map<String, List<Message>> groupBySubjectMsgs() {
        Map<String, List<Message>> messages = this.getMessages().stream()
                .collect(Collectors.groupingBy(Message::getSubject));

        return messages;
    }

    /**
     * Count number of words from users with certain names
     * @param name
     * @return number of words counted
     */
    public int countWordsNameMsgs(String name) {
        // Get usernames with particular name
        ArrayList<String> usernames = this.getUsers().stream()
                .filter((user) -> user.getName().equals(name))
                .map(user -> user.getUsername())
                .collect(Collectors.toCollection(ArrayList::new));

        // Count all the words of the messages
        int count = this.getMessages().stream()
                .filter((msg) -> usernames.contains(msg.getUsernameSender()))
                .mapToInt((msg) -> msg.getBody().split("\\s+").length).sum();

        return count;
    }

    /**
     * Get all the messages addressed to users with a birth year lower than
     * the last one by parameter
     * @param year
     * @return messages which meet the requirements
     */
    public ArrayList<Message> getByBornYearMsgs(int year) {
        // Get usernames with year of birth < year passed by parameter
        ArrayList<String> usernames = this.getUsers().stream()
                .filter((user) -> user.getYearBirth() < year)
                .map(user -> user.getUsername())
                .collect(Collectors.toCollection(ArrayList::new));

        // Get messages with usernames passed present
        ArrayList<Message> messages = this.getMessages().stream()
                .filter((msg) -> usernames.contains(msg.getUsernameReceiver()))
                .collect(Collectors.toCollection(ArrayList::new));

        return messages;
    }

    /**
     * Set new mail store
     * @param store
     */
    public void setStore(MailStore store) {
        this.store = store;
    }

    /**
     * Loads annotations and create new mailstore based on store() string, add
     * a dynamic proxy in case that log anotation is true
     */
    private void loadAnotation() {
        // Get metainformation of this class
        Class meta = getClass();

        // Get the anotation
        Annotation anot = meta.getAnnotation(Config.class);
        Config conf = (Config) anot;

        try {
            Class aClass = Class.forName(((Config) anot).store());
            // New instance alone it's deprecated
            store = (MailStore) aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (conf.log()) {
            this.store = (MailStore) DynamicProxy.newInstance(store);
        }
    }

}
