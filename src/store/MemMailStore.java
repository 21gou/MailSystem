package store;

import oop.MailStore;
import oop.Message;
import oop.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * MailStore based on in memory storage
 */
public class MemMailStore implements MailStore {
    private HashMap<String, ArrayList<Message>> store;

    public MemMailStore() {
        this.store = new HashMap<String, ArrayList<Message>>();
    }

    @Override
    public void sendMail(Message msg) {
        if(!store.containsKey(msg.getUsernameReceiver())) {
            this.store.put(msg.getUsernameReceiver(), new ArrayList<Message>());
        }

        this.store.get(msg.getUsernameReceiver()).add(msg);
    }

    @Override
    public ArrayList<Message> getMailsUser(User user) {
        if(store.containsKey(user.getUsername())) {
           return this.store.get(user.getUsername()).stream()
                   .map(msg -> msg.clone()) // Avoid modification inside store
                   .collect(Collectors.toCollection(ArrayList::new));
        } else {
            return new ArrayList<Message>();
        }
    }

    @Override
    public ArrayList<Message> getAllMessages() {
        ArrayList<Message> messages = this.store.entrySet().stream()
                .map(user -> user.getValue())
                .flatMap(Collection::stream)
                .map(msg -> msg.clone()) // Avoid modification inside store
                .collect(Collectors.toCollection(ArrayList::new));

        return messages;
    }

    @Override
    public long getNumMessages() {
        return (long)this.store.entrySet().stream()
                .mapToInt(user -> user.getValue().size())
                .sum();
    }
}
