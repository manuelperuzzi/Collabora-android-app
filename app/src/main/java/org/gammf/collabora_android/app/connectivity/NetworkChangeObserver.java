package org.gammf.collabora_android.app.connectivity;

/**
 * This interface represents a network change observer, which will be notified when network changes occur.
 */

public interface NetworkChangeObserver {
    /**
     * Callback called when the device successfully connects to the internet.
     */
    void onNetworkAvailable();

    /**
     * Callback called when the internet connection falls/is aborted.
     */
    void onNetworkUnavailable();
}
