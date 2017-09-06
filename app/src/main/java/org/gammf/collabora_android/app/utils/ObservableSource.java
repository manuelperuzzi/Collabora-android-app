package org.gammf.collabora_android.app.utils;

/**
 * A source of events of type T, that have to notify all the observers registered to its.
 * @param <T> the type of the events notified at the observers.
 */
public interface ObservableSource<T> {

    /**
     * Adds a new observer to the observers list.
     * @param observer the observer to be added.
     */
    void addObserver(Observer<? super T> observer);

    /**
     * Notifies all the observers registered in this source, with the passed event.
     * @param arg the event to be notified.
     */
    void notifyObservers(T arg);
}
