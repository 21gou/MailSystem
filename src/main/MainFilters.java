package main;

import filters.SpamUserFilter;
import filters.TooLongFilter;
import oop.MailBox;
import oop.MailStore;
import oop.Message;
import oop.User;
import patterns.MailStoreFactory;

import java.util.stream.Collectors;

/**
 * Demonstrates filters in action
 */
public class MainFilters {
    private static MailBox mailbox;
    private static MailStore store;

    public static void main(String[] args) {
        store = MailStoreFactory.createMailStore(MailStoreFactory.INMEMORYSTORE);
        loadData(store);

        // Get messages from username 1
        mailbox = new MailBox(new User("username 1", "name", 1990), store);

        System.out.println("MESSAGES IN MAILBOX OF username 1");
        mailbox.updateMail();
        for(Message msg: mailbox) {
            System.out.println(msg.toString());
        }

        System.out.println("ADD SpamUserFilterTest AND LIST MAILBOX:");
        mailbox.attach(new SpamUserFilter(mailbox));
        mailbox.updateMail();

        System.out.println("MAILBOX WITH SpamUserFilter:");
        for(Message msg: mailbox) {
            System.out.println(msg.toString());
        }

        System.out.println("SPAM LIST OF MAILBOX with SpamUserFilter:");
        for(Message msg: mailbox.listSpam()) {
            System.out.println(msg.toString());
        }

        System.out.println("ADD TooLongFilter AND LIST MAILBOX:");
        mailbox.attach(new TooLongFilter(mailbox));
        mailbox.updateMail();

        System.out.println("MAILBOX WITH TooLongFilter AND SpamUserFilter:");
        for(Message msg: mailbox) {
            System.out.println(msg.toString());
        }

        System.out.println("SPAM LIST OF MAILBOX with TooLongFilter:");
        for(Message msg: mailbox.listSpam()) {
            System.out.println(msg.toString());
        }

        System.out.println("USERS THAT SENDED MESSAGES FILTERED:");
        mailbox.listSpam().stream()
                .map(msg -> msg.getUsernameSender())
                .collect(Collectors.toSet())
                .forEach(System.out::println);

        System.out.println("DETTACH ALL THE FILTERS AND LIST MAIL:");
        mailbox.detach(new SpamUserFilter(mailbox));
        mailbox.detach(new TooLongFilter(mailbox));

        mailbox.updateMail();
        for(Message msg: mailbox.listMail()) {
            System.out.println(msg.toString());
        }

        System.out.println("LIST SPAM WITHOUT FILTERS:");
        for(Message msg: mailbox.listSpam()) {
            System.out.println(msg.toString());
        }

    }

    private static void loadData(MailStore store) {
        Message[] messages = new Message[] {
                new Message("Nice user", "username 1",
                        "Subject 1", "Nice body"),
                new Message("Bad spam user", "username 1",
                        "Subject 2", "Nice body too"),
                new Message("Bad body user 2", "username 1",
                        "Subject 3", "Body with a lot of data..."),
                new Message("Bad body user 3", "username 1",
                        "Subject 4", "Body with more than 20 characters"),
                new Message("spam user 2", "username 1",
                        "Subject 5", "Normal body"),
        };

        for(Message msg: messages) {
            store.sendMail(msg);
        }
    }
}
