package store;

import oop.MailStore;
import oop.Message;
import oop.Messages;
import oop.User;

public class RedisMailStore implements MailStore {
    @Override
    public boolean sendMail(Message msg) {
        return false;
    }

    @Override
    public Messages getMails(User user) {
        return null;
    }
}
