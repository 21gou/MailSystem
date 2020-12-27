package oop;

import reflective.Config;
import reflective.DynamicProxy;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

@Config(store="store.MemMailStore", log=true)
public class MailSystem {
    private HashMap<String, User> accounts;
    private HashMap<String, MailBox> mailboxes;

    private MailStore store;

    public MailSystem(MailStore store) {
        loadAnotation();

        this.accounts = new HashMap<String, User>();
        this.mailboxes = new HashMap<String, MailBox>();
    }

    /**
     *
     * @param user
     * @return
     */
    public boolean addUser(User user) {
        if(!accounts.containsKey(user.getUsername())) {
            accounts.put(user.getUsername(), user);
            mailboxes.put(user.getUsername(), new MailBox(user, store));
            return true;
        } else {
            return false;
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
     *
     * @return
     */
    public ArrayList<User> getUsers() {
        ArrayList<User> users = accounts.entrySet().stream()
                .map(entry -> entry.getValue())
                .collect(Collectors.toCollection(ArrayList::new));

        return users;
    }

    /**
     *
     * @param predicate
     * @return
     */
    public ArrayList<Message> filterMsgs(Predicate<? super Message> predicate) {
        return store.getAllMessages().stream()
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
        ArrayList<String> usernames = this.getUsers().stream()
                .filter((user) -> user.getName().equals(name))
                .map(user -> user.getUsername())
                .collect(Collectors.toCollection(ArrayList::new));

        // Count all the words of the messages
        int count = store.getAllMessages().stream()
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

    public void loadAnotation() {
        // Get metainformation of this class
        Class meta = getClass();

        // Get the anotation
        Annotation anot = meta.getAnnotation(Config.class);
        Config conf = (Config) anot;


        switch(conf.store()) {
            case "store.FileMailStore":
                store = MailStoreFactory.getMailStoreFactory(MailStoreFactory.FILESTORE);
                break;
            case "store.MemMailStore":
                store = MailStoreFactory.getMailStoreFactory(MailStoreFactory.INMEMORYSTORE);
                break;
            case "store.RedisMailStore":
                store = MailStoreFactory.getMailStoreFactory(MailStoreFactory.REDISSTORE);
                break;
            default:
                System.out.println("Error");
        }

        if (conf.log()) {
            this.store = (MailStore) DynamicProxy.newInstance(store);
        }
    }
}
