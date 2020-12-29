package patterns;

import oop.Message;

public abstract class ObservableMailBox {
    /**
     * Add new observable
     * @param filter
     */
    public abstract void attach(ObserverFilter filter);

    /**
     * Delete observable
     * @param filter
     */
    public abstract void detach(ObserverFilter filter);

    /**
     * Call for update from the subject class to the observers
     */
    public abstract void notifyObserver();

    /**
     * Move message from messages list to spam list
     * @param message
     */
    public abstract void setMessageSpam(Message message);
}
