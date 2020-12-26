package filters;

import filters.SpamUserFilter;
import oop.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import patterns.ObserverFilter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpamUserFilterTest {
    private User tmpUser;
    private MailBox mailbox;
    private MailStore store;
    private ObserverFilter filter;
    private ArrayList<Message> expectedMessages;

    @BeforeEach
    void setUp() {
        store = MailStoreFactory.getMailStoreFactory(MailStoreFactory.INMEMORYSTORE);
        tmpUser = new User("username 1", "name", 1990);
        mailbox = new MailBox(tmpUser, store);

        expectedMessages = new ArrayList<Message>();

        Message[] messages = {
                new Message("username 2", "username 1", "Example 1",
                        "Ex 1 body", Instant.now(), "b82c7d41-8c7c-4348-ab06-c044a9c91aa5"),
                new Message("username 2 spam", "username 1", "Example 2",
                        "Ex 2 body", Instant.now(),"c9b9f4df-78e1-4207-901d-87b34cbc19c8"),
                new Message("username spam 3", "username 1", "Example 3",
                        "Ex 3 body", Instant.parse("2020-12-21T06:42:25.627885692Z"), "uuid-10"),
                new Message("spam username 4", "username 1", "Example 4",
                        "Ex 4 body", Instant.parse("1970-01-01T00:00:00Z"), "uuid-5"),
                new Message("username 4", "username 1", "Example 5",
                        "Ex 5 body", Instant.now(), UUID.randomUUID().toString()),
                new Message("username 5", "username 1", "Example 6",
                        "Ex 6 body", Instant.now(), UUID.randomUUID().toString()),
        };

        for(Message msg: messages) {
            this.store.sendMail(msg);
            this.expectedMessages.add(msg);
        }
    }

    @Test
    void update() {
        // Check messages without filter
        mailbox.updateMail();

        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(expectedMessages, mailbox.listMail())
        );

        // Add filter and check messages
        filter = new SpamUserFilter(mailbox);
        mailbox.attach(filter);
        mailbox.updateMail();

        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(
                        expectedMessages.stream()
                                .filter(msg -> !msg.getUsernameSender().contains("spam"))
                                .collect(Collectors.toCollection(ArrayList::new)),
                        mailbox.listMail())
        );

        // List spam and check that there are filtered messages
        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(
                        expectedMessages.stream()
                                .filter(msg -> msg.getUsernameSender().contains("spam"))
                                .collect(Collectors.toCollection(ArrayList::new)),
                        mailbox.listSpam()
                )
        );

        // Dettach filter and check that messages filtered come back
        mailbox.detach(filter);
        mailbox.updateMail();

        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(expectedMessages, mailbox.listMail())
        );

        // Check list spam is empty
        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(new ArrayList<Message>(), mailbox.listSpam())
        );
    }
}
