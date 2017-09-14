package org.gammf.collabora_android.app.connectivity;

/**
 * This interface represents a network change observable, which is able to intercept network changes inside the application.
 */

public interface NetworkChangeObservable {
    /**
     * Registers a new {@link NetworkChangeObserver}, which will be notified on network changes.
     * @param observer the {@link NetworkChangeObserver} to be registered.
     * @return true if the {@link NetworkChangeObserver} is registered with success. False otherwise.
     */
    boolean addNetworkChangeObserver(NetworkChangeObserver observer);
}
