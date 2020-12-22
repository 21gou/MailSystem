package store;

import oop.MailStore;
import oop.Message;
import oop.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FileMailStore implements MailStore {
    private String filename;

    public FileMailStore() {
        this.filename = "./src/store/FileMailStore.data";
        checkFileAvaibility();
    }
    public FileMailStore(String filename) {
        this.filename = filename;
        checkFileAvaibility();
    }

    public void sendMail(Message msg) {
        try {
            Files.write(Path.of(filename), msg.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Message> getMailsUser(User user) {
        ArrayList<Message> messages = new ArrayList<Message>();

        try {
            messages = Files.lines(Path.of(this.filename))
                    .filter(msg -> msg.split(";")[1].equals(user.getUsername()))
                    .map(msg -> msg.split(";"))
                    .map(data -> new Message(data[0], data[1], data[2], data[3],
                            Instant.parse(data[4]), data[5]))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            System.out.println("damn");
            e.printStackTrace();
        }

        return messages;
    }

    public ArrayList<Message> getAllMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();

        try {
            messages = Files.lines(Path.of(this.filename))
                    .map(line -> line.split(";"))
                    .map(data -> new Message(data[0], data[1], data[2], data[3],
                            Instant.parse(data[4]), data[5]))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public long getNumMessages() {
        long numMessages = 0;

        try {
            numMessages = Files.lines(Path.of(this.filename)).count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return numMessages;
    }

    private void checkFileAvaibility() {
        try {
            if (!Files.exists(Path.of(filename))) {
                Files.createFile(Path.of(filename));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
