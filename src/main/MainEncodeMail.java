package main;

import oop.*;
import patterns.EncodeMailStoreDecorator;
import patterns.MailStoreFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Example of the Decorated MailStore that contains AES and Reverse strategy
 */
public class MainEncodeMail {
    private static MailBox mailbox;
    private static MailStore store;

    public static void main(String[] args) throws IOException {
        // Delete previous files in order to avoid errors
        deletePreviousFiles();

        System.out.println("LOADING FILESTORE WITH -AES- STRATEGY...");
        store = MailStoreFactory.createMailStore(MailStoreFactory.FILESTOREAES, "./src/main/StoreAES.data");
        mailbox = new MailBox(new User("username 1", "name", 1990), store);

        System.out.println("LOADING MESSAGES IN STORE DIRECTLY (avoid annotation injection)...");
        loadData(store);

        System.out.println("RETRIEVE MESSAGES OF username 1 MAILBOX: ");
        mailbox.updateMail();
        for(Message msg: mailbox) {
            System.out.println(msg.toString());
        }

        System.out.println("CONTENT IN FILE (-AES-): ");
        Files.lines(Path.of("./src/main/StoreAES.data")).forEach(System.out::println);

        System.out.println("-----------------------------------");

        System.out.println("LOADING FILESTORE WITH -REVERSE- STRATEGY...");
        store = MailStoreFactory.createMailStore(MailStoreFactory.FILESTOREREVERSE, "./src/main/StoreReverse.data");
        mailbox.setStore(store);

        System.out.println("LOADING MESSAGES IN STORE DIRECTLY (avoid annotation injection)...");
        loadData(store);

        System.out.println("RETRIEVE MESSAGES OF username 1 MAILBOX: ");
        mailbox.updateMail();
        for(Message msg: mailbox) {
            System.out.println(msg.toString());
        }

        System.out.println("CONTENT IN FILE (-Reverse-): ");
        Files.lines(Path.of("./src/main/StoreReverse.data")).forEach(System.out::println);

        System.out.println("-----------------------------------");

        System.out.println("LOADING FILESTORE WITH -AES AND REVERSE- STRATEGY...");
        store = MailStoreFactory.createMailStore(MailStoreFactory.FILESTOREAESREVERSE, "./src/main/StoreAESReverse.data");
        mailbox.setStore(store);

        System.out.println("LOADING MESSAGES IN STORE DIRECTLY (avoid annotation injection)...");
        loadData(store);

        System.out.println("RETRIEVE MESSAGES OF username 1 MAILBOX: ");
        mailbox.updateMail();
        for(Message msg: mailbox) {
            System.out.println(msg.toString());
        }

        System.out.println("CONTENT IN FILE (-AES and Reverse-): ");
        Files.lines(Path.of("./src/main/StoreAESReverse.data")).forEach(System.out::println);
    }

    /**
     * Load data directly to the store in order to bypass MailSystem default annotation store
     * @param store
     */
    private static void loadData(MailStore store) {
        // Load messages
        Message[] messages = new Message[] {
                new Message("username 1", "username 2", "Subject 1", "Body 1"),
                new Message("username 1", "username 3", "Subject 1", "Body 2"),
                new Message("username 1", "username 2", "Subject 2", "Body 2"),
                new Message("username 2", "username 1", "Subject", "Body 3"),
                new Message("username 3", "username 1", "Subject 2", "Body 4"),
                new Message("username 4", "username 1", "Subject 3", "Body 5"),
                new Message("username 5", "username 1", "Subject 5", "Body"),
        };

        for(Message msg: messages) {
            store.sendMail(msg);
        }
    }

    private static void deletePreviousFiles() throws IOException {
        // Delete files in order to prevent errors
        Files.deleteIfExists(Path.of("./src/main/StoreAES.data"));
        Files.deleteIfExists(Path.of("./src/main/StoreReverse.data"));
        Files.deleteIfExists(Path.of("./src/main/StoreAESAndReverse.data"));
    }

}
