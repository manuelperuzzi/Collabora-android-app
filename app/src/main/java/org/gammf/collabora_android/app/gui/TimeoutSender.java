package org.gammf.collabora_android.app.gui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.gammf.collabora_android.app.utils.IntentConstants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Alfredo Maffi
 * A simple class meant to send a network timeout to the MainActivity after a certain amount of time.
 */

public class TimeoutSender extends Timer {

    public TimeoutSender(final Context context, final long timeout) {
        this(context, timeout, null);
    }

    public TimeoutSender(final Context context, final long timeout, final String collaborationId) {

        this.schedule(new TimerTask() {
            @Override
            public void run() {
                final Intent intent = new Intent(MainActivity.getReceverIntentFilter());
                intent.putExtra(IntentConstants.MAIN_ACTIVITY_TAG, IntentConstants.TIMEOUT);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        }, timeout);
    }
}
