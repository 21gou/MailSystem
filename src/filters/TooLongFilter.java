package filters;

import oop.MailBox;
import oop.Message;
import patterns.ObserverFilter;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Moves to spam messages with more than 20 characters in body message
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TooLongFilter that = (TooLongFilter) o;
        return Objects.equals(filterSpam, that.filterSpam) && Objects.equals(mailbox, that.mailbox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterSpam, mailbox);
    }
}
