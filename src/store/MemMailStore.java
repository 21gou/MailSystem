package store;

import oop.MailStore;
import oop.Message;
import oop.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class MemMailStore implements MailStore {
    private HashMap<User, ArrayList<Message>> store;

    public MemMailStore() {
        this.store = new HashMap<User, ArrayList<Message>>();
    }

    public void sendMail(Message msg) {
        this.store.get(msg.getUsernameReceiver()).add(msg);
    }

    public ArrayList<Message> getMailsUser(User user) {
        return this.store.get(user);
    }

    public ArrayList<Message> getAllMessages() {
        ArrayList<Message> messages = this.store.entrySet().stream()
                .map(user -> user.getValue())
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));

        return messages;
    }

    public int getNumMessages() {
        return this.store.entrySet().stream()
                .mapToInt(user -> user.getValue().size())
                .sum();
    }
}
