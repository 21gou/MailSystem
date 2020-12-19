package oop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Messages implements Iterable<Message> {
    private ArrayList<Message> messages;

    public Messages() {
        this.messages = new ArrayList<Message>();
    }

    public Messages(ArrayList<Message> listMessages) {
        this.messages = listMessages;
    }

    public void addMessage(Message msg) {
        this.messages.add(msg);
    }

    public void addMessages(Messages newMessages) {
        for(Message msg: newMessages) {
            this.messages.add(msg);
        }
    }

    public int getNumMessages() {
        return this.messages.size();
    }

    public Stream<Message> stream() {
        return this.messages.stream();
    }

    public Iterator<Message> iterator() {
        return messages.iterator();
    }

    public void forEach(Consumer<? super Message> action) {
        messages.forEach(action);
    }
}
