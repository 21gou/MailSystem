package oop;

import java.util.Date;
import java.util.UUID;

public class Message {
    private final String uuid;
    private Date timestamp;

    private String usernameSender, usernameReceiver;
    private String subject, body;

    public Message(String usernameSender, String usernameReceiver) {
        this.usernameSender = usernameSender;
        this.usernameReceiver = usernameReceiver;

        this.timestamp = new java.util.Date();
        this.uuid = UUID.randomUUID().toString();
    }

    public Message(String usernameSender, String usernameReceiver, Date timestamp, String uuid) {
        this.usernameSender = usernameSender;
        this.usernameReceiver = usernameReceiver;

        this.timestamp = timestamp;
        this.uuid = uuid;
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
}
