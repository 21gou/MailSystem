package store;

import oop.MailStore;
import oop.Message;
import oop.Messages;
import oop.User;

import java.util.HashMap;

public class MemMailStore implements MailStore {
    private HashMap<User, Messages> store;

    public MemMailStore() {
        this.store = new HashMap<User, Messages>();
    }

    public void sendMail(Message msg) {
        this.store.get(msg.getUsernameReceiver()).addMessage(msg);
    }

    public Messages getMailsUser(User user) {
        return this.store.get(user);
    }

    public Messages getAllMessages() {
        Messages messages = new Messages();
        this.store.entrySet().stream()
                .forEach(user -> messages.addMessages(user.getValue()));

        return messages;
    }

    public int getNumMessages() {
        return this.store.entrySet().stream()
                .mapToInt(user -> user.getValue().getNumMessages())
                .sum();
    }
}
