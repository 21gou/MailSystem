package oop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Users implements Iterable<User> {
    private ArrayList<User> users;

    public Users() {
        this.users = new ArrayList<User>();
    }

    public Users(ArrayList<User> listUsers) {
        this.users = listUsers;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public boolean containsUsername(String username) {
        for(User user: users) {
            if(user.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    public Stream<User> stream() {
        return this.users.stream();
    }

    public Iterator<User> iterator() {
        return users.iterator();
    }

    public void forEach(Consumer<? super User> action) {
        users.forEach(action);
    }
}
