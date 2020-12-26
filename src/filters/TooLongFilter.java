package filters;

import oop.MailBox;
import oop.Message;
import patterns.ObserverFilter;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TooLongFilter extends ObserverFilter {
    private final Predicate<Message> filterSpam = (msg) -> msg.getBody().length() > 20;
    private MailBox mailbox;

    public TooLongFilter(MailBox mailbox) {
        this.mailbox = mailbox; 
    }

    public void update() {
        ArrayList<Message> spamDetected = this.mailbox.listMail().stream()
                .filter(filterSpam)
                .collect(Collectors.toCollection(ArrayList::new));

        spamDetected.forEach(msg -> this.mailbox.setMessageSpam(msg));
    }
}
