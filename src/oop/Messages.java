package oop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class Messages implements Iterable<Message> {
    private ArrayList<Message> messages = new ArrayList<Message>();

    public Messages() {

    }

    public Iterator<Message> iterator() {
        return messages.iterator();
    }

    public void forEach(Consumer<? super Message> action) {
        messages.forEach(action);
    }
}
