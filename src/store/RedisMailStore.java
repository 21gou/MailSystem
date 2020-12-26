package store;

import oop.MailStore;
import oop.Message;
import oop.User;
import redis.clients.jedis.Jedis;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class RedisMailStore implements MailStore {
    private Jedis conn;

    public RedisMailStore() {
        conn = new Jedis("localhost");
    }

    @Override
    public void sendMail(Message msg) {
        conn.lpush(msg.getUsernameReceiver(), msg.toString());
    }

    @Override
    public ArrayList<Message> getMailsUser(User user) {
        // Converts all the plain strings messages to list of object messages
        return conn.lrange(user.getUsername(), 0, -1).stream()
                .map(msg -> msg.split(";"))
                .map(data -> new Message(data[0], data[1], data[2], data[3],
                        Instant.parse(data[4]), data[5]))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Message> getAllMessages() {
        return conn.keys("*").stream()                  // Load all keys
                .map(key -> conn.lrange(key, 0, -1)) // Get all values from key
                .flatMap(Collection::stream)                   // Convert to stream
                .map(msg -> msg.split(";"))              // Split string
                .map(data -> new Message(data[0], data[1], data[2], data[3],
                        Instant.parse(data[4]), data[5]))       // Create new Message
                .collect(Collectors.toCollection(ArrayList::new));  // Collect all messages
    }

    @Override
    public long getNumMessages() {
        return this.getAllMessages().stream().count();
    }
}
