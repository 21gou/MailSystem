package filters;

import oop.MailBox;
import oop.Message;
import patterns.ObserverFilter;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SpamUserFilter extends ObserverFilter {
    private final Predicate<Message> filterSpam = (msg) -> msg.getUsernameSender().contains("spam");
    private MailBox mailbox;

    public SpamUserFilter(MailBox mailbox) {
        this.mailbox = mailbox;
    }

    public void update() {
        ArrayList<Message> spamDetected = this.mailbox.listMail().stream()
                .filter(filterSpam)
                .collect(Collectors.toCollection(ArrayList::new));

        spamDetected.forEach(msg -> this.mailbox.setMessageSpam(msg));
    }
}
