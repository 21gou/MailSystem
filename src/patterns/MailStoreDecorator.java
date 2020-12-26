package patterns;

import oop.MailStore;
import oop.Message;
import oop.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MailStoreDecorator implements MailStore {
    private MailStore store;

    public MailStoreDecorator(MailStore store) {
        this.store = store;
    }

    @Override
    public void sendMail(Message msg) {
        this.store.sendMail(msg);
    }

    @Override
    public ArrayList<Message> getMailsUser(User user) {
        return this.store.getMailsUser(user);
    }

    @Override
    public ArrayList<Message> getAllMessages() {
        return this.store.getAllMessages();
    }

    @Override
    public long getNumMessages() {
        return this.store.getNumMessages();
    }
}
