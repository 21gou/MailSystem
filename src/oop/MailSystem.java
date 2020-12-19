package oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

public class MailSystem {
    private HashMap<String, User> accounts;
    private MailStore store;

    public MailSystem(MailStore store) {
        this.store = store;
        this.accounts = new HashMap<String, User>();
    }

    /**
     *
     * @param user
     * @return
     */
    public boolean addUser(User user) {
        if(!accounts.containsKey(user.getUsername())) {
            accounts.put(user.getUsername(), user);
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return
     */
    public Messages getMessages() {
        return store.getAllMessages();
    }

    /**
     *
     * @return
     */
    public Users getUsers() {
        Users users = new Users();
        for(String usernameKeys: accounts.keySet()) {
            users.addUser(accounts.get(usernameKeys));
        }

        return users;
    }

    /**
     *
     * @param predicate
     * @return
     */
    public Messages filterMsgs(Predicate<? super Message> predicate) {
        return new Messages(store.getAllMessages().stream()
                .filter((Predicate<? super Message>) predicate)
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    /**
     * Get number of messages in the system
     * @return
     */
    public int countTotalMsgs() {
        return store.getNumMessages();
    }

    /**
     * Get average of messages per user
     * @return
     */
    public long getAverageUserMsgs() {
        return (long)store.getNumMessages()/(long)accounts.size();
    }

    /**
     * Return map of messages grouped by equal subject
     * @return grouped messages
     */
    public Map<String, List<Message>> groupBySubjectMsgs() {
        Map<String, List<Message>> messages = store.getAllMessages().stream()
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
        Users users = new Users(this.getUsers().stream()
                .filter((user) -> user.getName().equals(name))
                .collect(Collectors.toCollection(ArrayList::new)));

        // Count all the words of the messages
        int count = store.getAllMessages().stream()
                .filter((msg) -> users.containsUsername(msg.getUsernameSender()))
                .mapToInt((msg) -> msg.getBody().split("\\s+").length).sum();

        return count;
    }

    /**
     * Get all the messages addressed to users with a birth year lower than
     * the last one by parameter
     * @param year
     * @return messages which meet the requirements
     */
    public Messages getByBornYearMsgs(int year) {
        // Get usernames with year of birth < year passed by parameter
        Users users = new Users(this.getUsers().stream()
                .filter((user) -> user.getYearBirth() < year)
                .collect(Collectors.toCollection(ArrayList::new)));

        // Get messages with usernames passed present
        Messages msgs = new Messages(this.getMessages().stream()
                .filter((msg) -> users.containsUsername(msg.getUsernameReceiver()))
                .collect(Collectors.toCollection(ArrayList::new)));

        return msgs;
    }
}
