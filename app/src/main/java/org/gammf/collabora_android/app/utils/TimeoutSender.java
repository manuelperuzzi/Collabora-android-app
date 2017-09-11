package org.gammf.collabora_android.app.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.gammf.collabora_android.app.gui.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Alfredo Maffi
 * A simple class meant to send a network timeout to the MainActivity after a certain amount of time.
 */

public final class TimeoutSender extends Timer {
    public TimeoutSender(final Context context, final long timeout) {

        this.schedule(new TimerTask() {
            @Override
            public void run() {
                final Intent intent = new Intent(MainActivity.getReceiverIntentFilter());
                intent.putExtra(IntentConstants.MAIN_ACTIVITY_TAG, IntentConstants.TIMEOUT);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        }, timeout);
    }
}
