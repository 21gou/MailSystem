package oop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import patterns.MailStoreFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

public class MailBoxTest {
    private User tmpUser;
    private Message tmpMsg;
    private MailBox mailbox;
    private MailStore store;
    private ArrayList<Message> expectedMessages;

    @BeforeEach
    void setUp() {
        store = MailStoreFactory.createMailStore(MailStoreFactory.INMEMORYSTORE);
        expectedMessages = new ArrayList<Message>();
    }

    void loadData() {
        Message[] messages = {
                new Message("username 1", "username 2", "Example 1",
                        "Ex 1 body", Instant.now(), "b82c7d41-8c7c-4348-ab06-c044a9c91aa5"),
                new Message("username 2", "username 1", "Example 2",
                        "Ex 2 body", Instant.now() ,"c9b9f4df-78e1-4207-901d-87b34cbc19c8"),
                new Message("username 3", "username 1", "Example 3",
                        "Ex 3 body", Instant.parse("2020-12-21T06:42:25.627885692Z"), "uuid-10"),
                new Message("username 4", "username 3", "Example 4",
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

    @AfterEach
    void tearDown() {
    }

    @Test
    void updateAndListMail() {
        // Retrieve mail without any message
        tmpUser = new User("username 1", "name 1", 1999);
        mailbox = new MailBox(tmpUser, store);


        mailbox.updateMail();
        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(new ArrayList<Message>(), mailbox.listMail())
        );

        // Retrieve mail when messages are loaded
        loadData();

        mailbox.updateMail();
        expectedMessages = store.getAllMessages().stream()
                .filter(msg -> msg.getUsernameReceiver().equals(tmpUser.getUsername()))
                .collect(Collectors.toCollection(ArrayList::new));

        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(expectedMessages, mailbox.listMail())
        );

        // Retrieve mail from unknwon user
        tmpUser = new User("unknown", "unknown", 2000);
        mailbox = new MailBox(tmpUser, store);

        mailbox.updateMail();
        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(new ArrayList<Message>(), mailbox.listMail())
        );
    }

    @Test
    void sendMail() {
        // Send mail without any loaded yet
        tmpUser = new User("username 1", "name 1", 1999);
        mailbox = new MailBox(tmpUser, store);

        tmpMsg = new Message("username 2", "username 1", "Tmp message",
                "Body", Instant.now(), UUID.randomUUID().toString());

        mailbox.sendMail(tmpMsg);
        mailbox.updateMail();

        expectedMessages.add(tmpMsg);

        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(expectedMessages, mailbox.listMail())
        );

        // Load and send new email
        loadData();

        tmpMsg = new Message("username 2", "username 1", "Tmp message 2",
                "Body 2", Instant.now(), UUID.randomUUID().toString());

        mailbox.sendMail(tmpMsg);
        mailbox.updateMail();

        expectedMessages.add(tmpMsg);

        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(expectedMessages.stream()
                        .filter(msg -> msg.getUsernameReceiver().equals(tmpUser.getUsername()))
                        .collect(Collectors.toCollection(ArrayList<Message>::new)), mailbox.listMail())
        );
    }

    @Test
    void sortMail() {
        tmpUser = new User("username 1", "name 1", 1999);
        mailbox = new MailBox(tmpUser, store);

        // Sort by date whithout messages
        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(new ArrayList<Message>(),
                        mailbox.sortMail(Comparator.comparing(Message::getTimestamp)))
        );

        // Load and sort by date
        loadData();
        mailbox.updateMail();

        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(
                        expectedMessages.stream()
                                .filter(msg -> msg.getUsernameReceiver().equals(tmpUser.getUsername()))
                                .sorted(Comparator.comparing(Message::getTimestamp))
                                .collect(Collectors.toCollection(ArrayList::new)),
                        mailbox.sortMail(Comparator.comparing(Message::getTimestamp)))
        );

        // Sort by subject
        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(
                        expectedMessages.stream()
                                .filter(msg -> msg.getUsernameReceiver().equals(tmpUser.getUsername()))
                                .sorted(Comparator.comparing(Message::getTimestamp))
                                .collect(Collectors.toCollection(ArrayList::new)),
                        mailbox.sortMail(Comparator.comparing(Message::getSubject))
                )
        );
    }

    @Test
    void filterUserMail() {
        tmpUser = new User("username 1", "name 1", 1999);
        mailbox = new MailBox(tmpUser, store);

        // Filter messages from username 2 without messages loaded
        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(new ArrayList<Message>(), mailbox.listMail())
        );

        // Load messages and get messages sent from username 2
        loadData();
        mailbox.updateMail();

        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(
                        expectedMessages.stream()
                                .filter(msg -> msg.getUsernameReceiver().equals(tmpUser.getUsername()))
                                .filter(msg -> msg.getUsernameSender().equals("username 2"))
                                .collect(Collectors.toCollection(ArrayList::new)),
                        mailbox.filterUserMail(msg -> msg.getUsernameSender().equals("username 2")))
        );

        // Filter by subject
        Assertions.assertTrue(
                AssertContent.assertEqualDisordered(
                        expectedMessages.stream()
                                .filter(msg -> msg.getUsernameReceiver().equals(tmpUser.getUsername()))
                                .filter(msg -> msg.getSubject().equals("Example 2"))
                                .collect(Collectors.toCollection(ArrayList::new)),
                        mailbox.filterUserMail(msg -> msg.getSubject().equals("Example 2"))
                )
        );
    }
}
