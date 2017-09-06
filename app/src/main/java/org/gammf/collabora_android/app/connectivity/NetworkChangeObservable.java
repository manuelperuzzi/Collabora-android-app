package org.gammf.collabora_android.app.connectivity;

/**
 * Created by Alfredo on 05/09/2017.
 */

public interface NetworkChangeObservable {
    boolean addNetworkChangeObserver(NetworkChangeObserver observer);
    boolean removeNetworkChangeObserver(NetworkChangeObserver observer);
}
