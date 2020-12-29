package main;

import oop.*;
import patterns.MailStoreFactory;

import java.util.*;
import java.util.function.Predicate;

/**
 * Main of the MailSystem administrator, contains the basic operations of the class
 */
public class MainMailSystem {
    private static MailSystem system;

    public static void main(String[] args) {
        // 1. Initialize the mail system with an in-memory mail store.
        system = new MailSystem();

        // 2. Create at least 3 users, two have the same name but different username.
        User[] users = new User[]{
                new User("username 1", "name", 1990),
                new User("username 2", "name", 1995),
                new User("username 3", "name 2", 2001),
                new User("username 4", "Random name", 2005),
        };

        ArrayList<MailBox> mailBoxes = new ArrayList<MailBox>();
        for(User user: users) {
            mailBoxes.add(                  // Get mailboxes and store them for later
                    system.addUser(user)
            );
        }

        // 3. Then, use the mailboxes to send a few emails between them. Make some of them share the
        // same subject and make enough so that the following tests have results.
        Message[] messages = new Message[] {
                new Message("username 1", "username 2", "Subject 1", "Body 1"),
                new Message("username 1", "username 3", "Subject 1", "Body 2"),
                new Message("username 1", "username 2", "Subject 2", "Body 2"),
                new Message("username 2", "username 1", "Subject", "Body 3"),
                new Message("username 3", "username 1", "Subject 2", "Body 4"),
                new Message("username 2", "username 3", "Subject 3", "Body 5"),
                new Message("username 3", "username 1", "Subject 10", "Body 1"),
                new Message("username 2", "username 1", "Subject 7", "Body 9"),
                new Message("username 2", "username 1", "Sub", "Rare body"),
                new Message("username 3", "username 1", "Sub", "Rare body 2"),
        };

        for(Message msg: messages) {
            switch (msg.getUsernameSender()) {
                case "username 1":
                    mailBoxes.get(0).sendMail(msg);
                    break;
                case "username 2":
                    mailBoxes.get(1).sendMail(msg);
                    break;
                case "username 3":
                    mailBoxes.get(2).sendMail(msg);
                    break;
                default:
                    System.out.println("Unexpected error sending mails");
            }
        }

        // 4. Get one of the mailboxes and update its mail.
        mailBoxes.get(0).updateMail();

        // 5. List the mailbox messages in the console. (Sorted by newer first.) Use the iterable capabilities
        // of the mailbox!
        System.out.println("MESSAGES FROM USERNAME 1 (sorted by date): ");
        for(Message msg: mailBoxes.get(0).sortMail(Comparator.comparing(Message::getTimestamp))) {
            System.out.println(msg.toString());
        }

        System.out.println("MESSAGES FROM USERNAME 1 (sorted by date) EXPECTED: ");
        for(Message msg: messages) {
            if(msg.getUsernameReceiver().equals("username 1")) {        // Already sorted by time
                System.out.println(msg.toString());
            }
        }

        // 6. Now list the messages by sender username using the mailbox feature.
        System.out.println("MESSAGES FROM USERNAME 1 (sorted by username sender): ");
        for(Message msg: mailBoxes.get(0).sortMail(Comparator.comparing(Message::getUsernameSender))) {
            System.out.println(msg.toString());
        }

        // 7. Filter the messages with the following conditions:
        //      - The message subject contains a certain word.
        //      - The message sender is a certain user.
        System.out.println("MESSAGES FILTERED BY CERTAIN WORD (Rare) AND SENDER (username 2): ");
        Predicate<Message> lambdaFilter = msg -> {
            return msg.getBody().contains("Rare") && msg.getUsernameSender().equals("username 2");
        };

        for(Message msg: mailBoxes.get(0).filterUserMail(lambdaFilter)) {
            System.out.println(msg.toString());
        }

        // 8. Use the mail system object to retrieve all messages and print them.
        System.out.println("GET ALL THE MESSAGES FROM MAILSYSTEM: ");
        for(Message msg: system.getMessages()) {
            System.out.println(msg.toString());
        }

        // 9. Filter messages globally that fulfill the following conditions:
        //      - The message subject is a single word.
        //      - The sender was born after year 2000.
        System.out.println("MESSAGES WITH USERS BORN BEFORE 2000 AND SUBJECTS OF LENGTH 1");
        system.getByBornYearMsgs(2000).stream()
                .filter(msg -> msg.getSubject().split("\\s+").length == 1)
                .map(msg -> msg.toString())
                .forEach(System.out::println);


        // 10. Get the count of messages in the system and print it.
        System.out.println("NUMBER OF MESSAGES: "+system.countTotalMsgs());

        // 11. Get the average number of messages received per user and print it.
        System.out.println("AVERAGE MESSAGES PER USER: "+system.getAverageUserMsgs());

        // 12. Group the messages per subject in a Map<String, List<Message>> and print it.
        System.out.println("MESSAGES GROUPED BY SUBJECT: ");
        Map<String, List<Message>> messagesGrouped = system.groupBySubjectMsgs();
        for(String key: messagesGrouped.keySet()) {
            System.out.println("-Subject: "+key+", Messages: ");
            for(Message msg: messagesGrouped.get(key)) {
                System.out.println(msg.toString());
            }
        }

        // 13. Count the words of all messages sent by users with a certain real name.
        System.out.println("NUMBER OF WORDS OF USERS WITH NAME 'name 2': "
                +system.countWordsNameMsgs("name 2"));

        System.out.println("NUMBER OF WORDS OF USERS WITH NAME 'Random name' (0): "
                +system.countWordsNameMsgs("Random name"));

        // 14. Use the name that you used on two users. Print the result.
        System.out.println("NUMBER OF WORDS OF USERS WITH NAME 'name': "
                +system.countWordsNameMsgs("name"));

        // 15. Print the messages received by users born before year 2000.
        System.out.println("MESSAGES FROM USERS BORN BEFORE 2000: ");
        for(Message msg: system.getByBornYearMsgs(2000)) { // Will print messages from username 1 and username 2
            System.out.println(msg.toString());
        }

        // 16. Now change the mail store to the file implementation.
        system.setStore(MailStoreFactory.createMailStore(MailStoreFactory.FILESTORE));
    }
}
