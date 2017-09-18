package org.gammf.collabora_android.app.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple abstract class that provides basic implementation of a {@link ObservableSource}.
 * @param <T> the type of notified events.
 */
public class AbstractObservableSource<T> implements ObservableSource<T> {

    private final Set<Observer<? super T>> observersSet = new HashSet<>();

    @Override
    public void addObserver(final Observer<? super T> observer) {
        this.observersSet.add(observer);
    }

    @Override
    public void notifyObservers(final T arg) {
        for (final Observer<? super T> observer : this.observersSet) {
            observer.notify(arg);
        }
    }
}
