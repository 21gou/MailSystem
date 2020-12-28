package main;

import oop.*;
import patterns.MailStoreFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Scanner;

public class MainCLI {
    private static MailSystem system;
    private static MailBox mailbox;
    private static MailStore store;

    private static String username;
    private static Scanner scanner;

    /**
     * Interactive CLI for demonstrate the system work, have admin and user mode
     * check showOptions to see possible operations
     * @param args
     */
    public static void main(String[] args) throws IOException {
        String[] splitCommand;
        boolean exitProgram = false;

        mailbox = null;
        system = new MailSystem();
        store = MailStoreFactory.createMailStore(MailStoreFactory.FILESTORE, "./src/main/MainCLI.data");

        scanner = new Scanner(System.in);

        loadInformation(system, store);
        showOptions();

        while(!exitProgram) {
            System.out.println();
            if(mailbox == null) {
                System.out.print("admin> ");
                exitProgram = getCommandAdmin(scanner.nextLine().split(" "));
            } else {
                System.out.print(username+"> ");
                exitProgram = getCommandUserMode(scanner.nextLine().split(" "));
            }
        }

        Files.deleteIfExists(Path.of("./src/main/MainCLI.data"));
    }

    /**
     * Load some test information for the CLI interaction
     * @param system
     * @param store
     */
    private static void loadInformation(MailSystem system, MailStore store) {
        system.setStore(store);

        User[] users = {
                new User("user1", "name1", 1990),
                new User("user2", "name2", 2000),
                new User("user3", "name3", 2005),
                new User("user4", "name4", 2010),
        };

        for(User user: users) {
            system.addUser(user);
        }

        Message[] messages = {
                new Message("user1", "user2", "Subject", "Example body"),
                new Message("user1", "user2", "Subject 2", "Example body 2"),
                new Message("user2", "user1", "Subject 3", "Example body 3"),
                new Message("user2", "user1", "Subject 4", "Example body 4"),
                new Message("user2", "user1", "Subject 5", "Example body 5"),
                new Message("user2", "user1", "Subject 6", "Example body 6"),
        };

        for(Message msg: messages) {
            store.sendMail(msg);
        }
    }

    private static void showOptions() {
        System.out.println("OPTIONS AVAIBLE FOR ADMIN: ");
        // User not logged
        System.out.println("\tcreateuser <username> <args>... : Create a new user as admin");
        System.out.println("\tfilter <...> : Filter at a system level.");
        System.out.println("\t\t- contains <word>");
        System.out.println("\t\t- lessthan <n>");
        System.out.println("\tlogas <username> : Log in as a user. No passwords.");

        System.out.println("OPTIONS AVAIBLE FOR USER LOGGED: ");
        // User already logged
        System.out.println("\tsend <to> 'subject' 'body' : send a new message.");
        System.out.println("\tupdate : retrieve messages from the mail store.");
        System.out.println("\tlist : show messages sorted by sent time.");
        System.out.println("\tsort <...> : sort messages by some predefined comparators.");
        System.out.println("\t\t- time : sort by time of message creation");
        System.out.println("\t\t- subject : sort by subject alphabetical order");
        System.out.println("\tfilter : same options that for system, but just for the user messages.");
        System.out.println("\t\t- contains <word>");
        System.out.println("\t\t- lessthan <n>\n");

        System.out.println("exit : close the program");
        System.out.println("help : print options avaible");
    }

    /**
     * Check command for admin inputs
     * @param splitCommand
     */
    private static boolean getCommandAdmin(String[] splitCommand) {

        switch(splitCommand[0]) {
            case "createuser":
                if(splitCommand.length == 4) {
                    try {
                        system.addUser(new User(splitCommand[1], splitCommand[2],
                                Integer.valueOf(splitCommand[3])));
                        System.out.println("[*] User created succesfully\n");
                    }catch (Exception e) {
                        System.out.println("Format of year not valid!\n");
                    }
                } else {
                    System.out.println("Create user format: ");
                    System.out.println("\tcreateuser <username> <name> <year>");
                }
                break;

            case "filter":
                if(splitCommand.length == 3) {
                    if(splitCommand[1].equals("contains")) {
                        System.out.println("Messages filtered: ");
                        system.filterMsgs(msg -> msg.getBody().contains(splitCommand[2])).stream()
                                .map(msg -> msg.toString())
                                .forEach(System.out::println);
                    } else if(splitCommand[1].equals("lessthan")) {
                        try {
                            system.filterMsgs(msg -> msg.getBody().length() < Integer.valueOf(splitCommand[2])).stream()
                                    .map(msg -> msg.toString())
                                    .forEach(System.out::println);

                        } catch (Exception e) {
                            System.out.println("Format of number of messages not valid!");
                        }
                    } else {
                        System.out.println("Filter second argument not valid");
                    }
                } else {
                    System.out.println("Filter messages format: ");
                    System.out.println("\tfilter contains|lessthan <word|number>");
                }
                break;

            case "logas":
                if(splitCommand.length == 2) {
                    mailbox = system.logIn(splitCommand[1]);
                    if(mailbox != null) {
                        username = splitCommand[1];
                        mailbox.updateMail();

                        System.out.println("[*] User logged succesfully!");
                    } else {
                        System.out.println("User not created yet, cannot acces mailbox!");
                    }
                } else {
                    System.out.println("Logas format: ");
                    System.out.println("\tlogas <username>");
                }
                break;
            case "help":
                showOptions();
                break;
            case "exit":
                return true;
            default:
                System.out.println("Unexpected error getting command");
                break;
        }

        return false;
    }

    /**
     *
     * @param splitCommand
     */
    private static boolean getCommandUserMode(String[] splitCommand) {
        switch (splitCommand[0]) {
            case "send":
                if (splitCommand.length == 4) {
                    mailbox.sendMail(new Message(username, splitCommand[1], splitCommand[2], splitCommand[3]));
                    System.out.println("[*] Mail sended successfully");
                }
                break;
            case "update":
                mailbox.updateMail();
                System.out.println("[*] Mail updated successfully");
                break;
            case "list":
                System.out.println("Mails in mailbox: ");
                mailbox.listMail().stream()
                        .map(msg -> msg.toString())
                        .forEach(System.out::println);
                break;
            case "sort":
                if (splitCommand.length == 2) {
                    if (splitCommand[1].equals("time")) {
                        System.out.println("Messages sorted by time of creation: ");
                        mailbox.sortMail(Comparator.comparing(Message::getTimestamp)).stream()
                                .map(msg -> msg.toString())
                                .forEach(System.out::println);

                    } else if (splitCommand[1].equals("subject")) {
                        System.out.println("Messages sorted by subject in alphabetical order: ");
                        mailbox.sortMail(Comparator.comparing(Message::getSubject)).stream()
                                .map(msg -> msg.toString())
                                .forEach(System.out::println);
                    } else {
                        System.out.println("Selected option not avaible!");
                        System.out.println("Options avaible: sort time or sort subject");
                    }
                } else {
                    System.out.println("Sort format: ");
                    System.out.println("\tsort time : sort by time of creation");
                    System.out.println("\tsort subject : sort by subject alphabetical");
                }
                break;
            case "filter":
                if (splitCommand.length == 3) {
                    if (splitCommand[1].equals("contains")) {
                        System.out.println("Messages filtered: ");
                        mailbox.filterUserMail(msg -> msg.getBody().contains(splitCommand[2])).stream()
                                .map(msg -> msg.toString())
                                .forEach(System.out::println);
                    } else if (splitCommand[1].equals("lessthan")) {
                        try {
                            mailbox.filterUserMail(msg -> msg.getBody().length() < Integer.valueOf(splitCommand[2])).stream()
                                    .map(msg -> msg.toString())
                                    .forEach(System.out::println);

                        } catch (Exception e) {
                            System.out.println("Format of number of messages not valid!");
                        }
                    } else {
                        System.out.println("Filter second argument not valid");
                    }
                } else {
                    System.out.println("Filter messages format: ");
                    System.out.println("\tfilter contains|lessthan <word|number>");
                }
                break;
            case "help":
                showOptions();
                break;
            case "exit":
                return true;
            default:
                System.out.println("Unexpected error getting command");
        }

        return false;
    }
}
