package org.gammf.collabora_android.app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.gammf.collabora_android.app.BootService;

/**
 * BroadcastReceiver called when device is rebooted
 * Created by Federico on 04/08/2017.
 */

public class RestartReceiver extends BroadcastReceiver {

    public static final String TAG = "RestartReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            // It is better to reset alarms using Background IntentService
            Intent i = new Intent(context, BootService.class);
            ComponentName service = context.startService(i);

            if (null == service) {
                // something really wrong here
                Log.e(TAG, "Could not start service ");
            }
            else {
                Log.e(TAG, "Successfully started service ");
            }

        } else {
            Log.e(TAG, "Received unexpected intent " + intent.toString());
        }
    }
}
