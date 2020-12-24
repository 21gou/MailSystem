package patterns;

import oop.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class TooLongFilterTest {
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
                        "Ex 1 body", Instant.now(), UUID.randomUUID().toString()),
                new Message("username 2 spam", "username 1", "Example 2",
                        "Ex 2 body", Instant.now(),UUID.randomUUID().toString() ),
                new Message("username spam 3", "username 1", "Example 3",
                        "Body with 21 characts", Instant.now(), UUID.randomUUID().toString()),
                new Message("spam username 4", "username 1", "Example 4",
                        "Body with 20 charact", Instant.now(), UUID.randomUUID().toString()),
                new Message("username 4", "username 1", "Example 5",
                        "Body with more than 32 characters", Instant.now(), UUID.randomUUID().toString()),
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
        filter = new TooLongFilter(mailbox);
        mailbox.attach(filter);
        mailbox.updateMail();

        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(
                        expectedMessages.stream()
                                .filter(msg -> !(msg.getBody().length() > 20))
                                .collect(Collectors.toCollection(ArrayList::new)),
                        mailbox.listMail())
        );

        // List spam and check that there are filtered messages
        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(
                        expectedMessages.stream()
                                .filter(msg -> msg.getBody().length() > 20)
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
