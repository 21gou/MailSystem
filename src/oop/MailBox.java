package oop;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class MailBox implements Iterable<Message> {
    private ArrayList<Message> messages;

    public MailBox() {
        this.messages = new ArrayList<Message>();
    }

    public MailBox(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void updateMail() {

    }

    public void listMail() {

    }

    public void sendMail() {

    }

    // with filte
    public void getFilterMail() {

    }

    // Same than filter mail?
    public void filterUserMail() {

    }

    @Override
    public Iterator<Message> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super Message> action) {

    }
}
