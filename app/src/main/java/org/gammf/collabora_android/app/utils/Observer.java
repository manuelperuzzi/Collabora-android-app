package org.gammf.collabora_android.app.utils;

/**
 * A utility class for observer design pattern
 * @param <T> The type of the update notified.
 */
public interface Observer<T> {

    /**
     * Notify the observer of a changement in the source.
     * This method have to be called by the event source.
     * @param arg the changement reported.
     */
    void notify(T arg);
}
