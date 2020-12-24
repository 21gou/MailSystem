package patterns;

import oop.Message;

public abstract class ObservableMailBox {
    public abstract void attach(ObserverFilter filter);
    public abstract void detach(ObserverFilter filter);
    public abstract void notifyObserver();

    public abstract void setMessageSpam(Message message);
}
