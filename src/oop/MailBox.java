package oop;

import java.util.Iterator;
import java.util.function.Consumer;

public class MailBox implements Iterable<Message> {
    private Messages messages;

    public MailBox() {
        this.messages = new Messages();
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
