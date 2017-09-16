package org.gammf.collabora_android.app.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link BroadcastReceiver} meant to manage connectivity changes and notify all the previously registered {@link NetworkChangeObserver}.
 */

public class NetworkChangeManager extends BroadcastReceiver implements NetworkChangeObservable{

    private static NetworkChangeManager instance;

    private final Set<NetworkChangeObserver> observers;

    private NetworkChangeManager() {
        this.observers = new HashSet<>();
    }

    /**
     * Singleton pattern.
     * @return the {@link NetworkChangeManager} unique instance.
     */
    public static NetworkChangeManager getInstance() {
        if(instance == null) {
            instance = new NetworkChangeManager();
        }
        return instance;
    }


    @Override
    public void onReceive(final Context context, final Intent intent) {
        if(intent != null && intent.getExtras() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!= null && networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                this.notifyAllObservers(NetworkChange.AVAILABLE);

            } else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                this.notifyAllObservers(NetworkChange.UNAVAILABLE);
            }
        }
    }

    public boolean addNetworkChangeObserver(final NetworkChangeObserver observer) {
        return this.observers.add(observer);
    }

    public void clearObservers() {
        this.observers.clear();
    }

    private void notifyAllObservers(NetworkChange change) {
        for(final NetworkChangeObserver listener : this.observers) {
            if(change.equals(NetworkChange.AVAILABLE)) {
                listener.onNetworkAvailable();
            } else if(change.equals(NetworkChange.UNAVAILABLE)) {
                listener.onNetworkUnavailable();
            }
        }
    }
}
