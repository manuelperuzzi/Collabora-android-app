package org.gammf.collabora_android.app.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.gammf.collabora_android.app.gui.MainActivity;

public class ExceptionManager {

    private static final ExceptionManager EXCEPTION_MANAGER = new ExceptionManager();

    private static MainActivity mainActivity;

    private ExceptionManager() { }

    public static ExceptionManager getInstance() {
        return EXCEPTION_MANAGER;
    }

    public static void init(final MainActivity mainActivity) {
        ExceptionManager.mainActivity = mainActivity;
    }

    public void handle(final Exception e) {
        /*Log.i("FLUSSOANDROID", "exceptionManager");
        e.printStackTrace();
        final Intent intent = new Intent(MainActivity.getReceiverIntentFilter());
        intent.putExtra(IntentConstants.MAIN_ACTIVITY_TAG, IntentConstants.LOCAL_STORAGE_ERROR);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);*/
        //context.onUserLogout();
        mainActivity.onLocalStorageCorrupted();
    }
}
