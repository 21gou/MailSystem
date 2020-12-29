package filters;

import oop.MailBox;
import oop.Message;
import patterns.ObserverFilter;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Moves to spam messages whos sender contains the word "spam" as username
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpamUserFilter that = (SpamUserFilter) o;
        return Objects.equals(filterSpam, that.filterSpam) && Objects.equals(mailbox, that.mailbox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterSpam, mailbox);
    }
}
