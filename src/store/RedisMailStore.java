package store;

import oop.MailStore;
import oop.Message;
import oop.User;

import java.util.ArrayList;

public class RedisMailStore implements MailStore {
    @Override
    public void sendMail(Message msg) {

    }

    @Override
    public ArrayList<Message> getMailsUser(User user) {
        return null;
    }

    @Override
    public ArrayList<Message> getAllMessages() {
        return null;
    }

    @Override
    public long getNumMessages() {
        return 0;
    }
}
