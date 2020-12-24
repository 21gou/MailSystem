package oop;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Message {
    private final String uuid;
    private Instant timestamp;

    private String usernameSender, usernameReceiver;
    private String subject, body;

    public Message(String usernameSender, String usernameReceiver, String subject, String body) {
        this.usernameSender = usernameSender;
        this.usernameReceiver = usernameReceiver;

        this.timestamp = Instant.now();
        this.uuid = UUID.randomUUID().toString();

        this.subject = subject;
        this.body = body;
    }

    public Message(String usernameSender, String usernameReceiver, String subject, String body, Instant timestamp, String uuid) {
        this.usernameSender = usernameSender;
        this.usernameReceiver = usernameReceiver;

        this.timestamp = timestamp;
        this.uuid = uuid;

        this.subject = subject;
        this.body = body;
    }

    public String getUsernameSender() {
        return this.usernameSender;
    }

    public String getUsernameReceiver() {
        return this.usernameReceiver;
    }

    public String getSubject() { return this.subject; }
    public String getBody() {
        return this.body;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public String getUuid() {
        return this.uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(getUuid(), message.getUuid()) && Objects.equals(getTimestamp(),
                message.getTimestamp()) && Objects.equals(getUsernameSender(),
                message.getUsernameSender()) && Objects.equals(getUsernameReceiver(),
                message.getUsernameReceiver()) && Objects.equals(getSubject(),
                message.getSubject()) && Objects.equals(getBody(), message.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, timestamp, usernameSender, usernameReceiver, subject, body);
    }

    @Override
    public String toString() {
        return usernameSender+";"+usernameReceiver+";"+subject+";"+body+";"+timestamp.toString()+";"+uuid+"\n";
    }

    @Override
    public Message clone() {
        return new Message(usernameSender, usernameReceiver, subject, body, timestamp, uuid);
    }
}
