package store;

import oop.MailStore;
import oop.Message;
import oop.Messages;
import oop.User;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Date;

public class FileMailStore implements MailStore {
    private String filename;

    public FileMailStore() {
        this.filename = "./src/store/FileMailStore.data";
    }

    public void sendMail(Message msg) {
        try {
            Files.write(Path.of(filename), msg.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Messages getMailsUser(User user) {
        Messages messages = new Messages();

        try {
            Files.lines(Path.of(this.filename))
                    .filter(msg -> msg.split(";")[1].equals(user.getUsername()))
                    .map(msg -> msg.split(";"))
                    .forEach(data -> messages.addMessage(
                            new Message(data[0], data[1], data[2], data[3], Date.valueOf(data[4]), data[5]))
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public Messages getAllMessages() {
        Messages messages = new Messages();

        try {
            Files.lines(Path.of(this.filename))
                    .map(line -> line.split(";"))
                    .forEach(data -> messages.addMessage(
                            new Message(data[0], data[1], data[2], data[3], Date.valueOf(data[4]), data[5])
                    ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public int getNumMessages() {
        int numMessages = 0;

        try {
            numMessages = (int)Files.lines(Path.of(this.filename)).count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return numMessages;
    }
}
