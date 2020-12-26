package oop;

import org.junit.jupiter.api.*;
import redis.clients.jedis.Jedis;
import store.FileMailStore;
import store.MemMailStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MailStoreTest {
    private MailStore[] stores;
    private ArrayList<Message> expectedMessages;

    @BeforeEach
    void setUp() {
        try {
            Files.deleteIfExists(Path.of("./test/oop/FileMailStoreTest.data"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedMessages = new ArrayList<Message>();

        MailStoreFactory storeFactory = new MailStoreFactory();

        stores = new MailStore[]{
                storeFactory.getMailStoreFactory(MailStoreFactory.INMEMORYSTORE),
                storeFactory.getMailStoreFactory(MailStoreFactory.FILESTORE, "./test/oop/FileMailStoreTest.data"),
                storeFactory.getMailStoreFactory(MailStoreFactory.REDISSTORE),
        };
    }

    @AfterEach
    void tearDown() {
        try {
            Files.deleteIfExists(Path.of("./test/oop/FileMailStoreTest.data"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Jedis jedis = new Jedis("localhost");
        jedis.flushAll();
        jedis.close();
    }

    void loadData() {
        Message[] messages = {
                new Message("username 1", "username 3", "subject 1", "body 1", Instant.now(), UUID.randomUUID().toString()),
                new Message("username 2", "username 1", "subject 2", "body 2", Instant.now(), UUID.randomUUID().toString()),
                new Message("username 3", "username 2", "subject 3", "body 3", Instant.now(), UUID.randomUUID().toString()),
        };

        for(Message msg: messages) {
            expectedMessages.add(msg);
            for(MailStore store: stores) {
                store.sendMail(msg);
            }
        }
    }



    @Test
    void sendMail() {
        // Check if fails being the first message
        Message newMsg = new Message("username 4", "username 2", "subject 4",
                "body 4", Instant.now(), UUID.randomUUID().toString());

        expectedMessages.add(newMsg);
        for(MailStore store: stores) {
            store.sendMail(newMsg);
        }

        for(MailStore store: stores) {
            Assertions.assertTrue(
                    AssertContent.assertEqualDisordered(expectedMessages, store.getAllMessages())
            );
        }

        // Load all messages and try to send another
        loadData();
        newMsg = new Message("username 5", "username 1", "subject 5",
                "body 5", Instant.now(), UUID.randomUUID().toString());

        expectedMessages.add(newMsg);
        for(MailStore store: stores) {
            store.sendMail(newMsg);
        }

        for(MailStore store: stores) {
            Assertions.assertTrue(
                    AssertContent.assertEqualDisordered(expectedMessages, store.getAllMessages())
            );
        }
    }

    @Test
    void getMailsUser() {
        // Get mails of user empty
        User tmpUser = new User("username 1", "name 1", 2000);

        for(MailStore store: stores) {
            Assertions.assertTrue(
                    AssertContent.assertEqualDisordered(new ArrayList<Message>(), store.getMailsUser(tmpUser))
            );
        }

        // Load all messages and try to retrieve
        loadData();

        expectedMessages = expectedMessages.stream()
                .filter(msg -> msg.getUsernameReceiver().equals("username 1"))
                .collect(Collectors.toCollection(ArrayList::new));

        for(MailStore store: stores) {
            Assertions.assertTrue(
                    AssertContent.assertEqualDisordered(expectedMessages, store.getMailsUser(tmpUser))
            );
        }

        // Load messages from user not known
        tmpUser = new User("unknown", "unknown", 2000);

        for(MailStore store: stores) {
            store.getMailsUser(tmpUser);
            Assertions.assertTrue(
                    AssertContent.assertEqualDisordered(new ArrayList<Message>(), store.getMailsUser(tmpUser))
            );
        }

    }

    @Test
    void getAllMessages() {
        // Get empty message store
        for(MailStore store: stores) {
            Assertions.assertTrue(
                    AssertContent.assertEqualDisordered(new ArrayList<Message>(), store.getAllMessages())
            );
        }

        // Load messages and try to get them
        loadData();
        for(MailStore store: stores) {
            Assertions.assertTrue(
                    AssertContent.assertEqualDisordered(expectedMessages, store.getAllMessages())
            );
        }
    }

    @Test
    void getNumMessages() {
        // Get number of empty store
        for(MailStore store: stores) {
            Assertions.assertTrue(expectedMessages.size() == store.getNumMessages());
        }

        // Load messages and get number
        loadData();
        for(MailStore store: stores) {
            Assertions.assertTrue(expectedMessages.size() == store.getNumMessages());
        }
    }
}
