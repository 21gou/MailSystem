package patterns;

import oop.MailStore;
import oop.Message;
import oop.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Encode body of the messages with a given strategy
 */
public class EncodeMailStoreDecorator extends MailStoreDecorator {
    private EncodeStrategy cipher;

    public EncodeMailStoreDecorator(MailStore store, EncodeStrategy cipher) {
        super(store);
        this.cipher = cipher;
    }

    @Override
    public void sendMail(Message msg) {
        msg.setBody(
                cipher.encode(msg.getBody())
        );

        super.sendMail(msg);
    }

    @Override
    public ArrayList<Message> getMailsUser(User user) {
        return super.getMailsUser(user).stream()
                .map(msg -> {
                    msg.setBody(cipher.decode(msg.getBody()));
                    return msg;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Message> getAllMessages() {
        return super.getAllMessages().stream()
                .map(msg -> {
                    msg.setBody(cipher.decode(msg.getBody()));
                    return msg;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
