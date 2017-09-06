package org.gammf.collabora_android.app.gui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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
                final Intent intent = new Intent("update.collaborations.on.gui");
                intent.putExtra("timeout", timeout);
                if(collaborationId != null) {
                    intent.putExtra("collaborationId", collaborationId);
                }
                Log.i("Test","5) TimeoutSender - eeeh");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        }, timeout);
    }
}
