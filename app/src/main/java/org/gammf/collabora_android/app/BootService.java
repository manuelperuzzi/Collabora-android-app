package org.gammf.collabora_android.app;

import android.app.IntentService;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Federico on 04/08/2017.
 */

public class BootService extends IntentService {

    public static final String PREFS_NAME = "CollaboraPrefs";

    public BootService() {
        super("BootService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // reset here all allarms
        //take from db or saved locally collection...
        Utility utility = new Utility();

        Calendar firstTry = Calendar.getInstance();
        firstTry.set(Calendar.YEAR, 2017);
        firstTry.set(Calendar.MONTH, 7);
        firstTry.set(Calendar.DAY_OF_MONTH, 4);
        firstTry.set(Calendar.HOUR_OF_DAY, 10);
        firstTry.set(Calendar.MINUTE, 53);
        firstTry.set(Calendar.SECOND, 0);

        Calendar secondTry = Calendar.getInstance();
        secondTry.set(Calendar.YEAR, 2017);
        secondTry.set(Calendar.MONTH, 7);
        secondTry.set(Calendar.DAY_OF_MONTH, 4);
        secondTry.set(Calendar.HOUR_OF_DAY, 10);
        secondTry.set(Calendar.MINUTE, 54);
        secondTry.set(Calendar.SECOND, 0);

        utility.setAlarm(this,"First Event",firstTry);
        utility.setAlarm(this,"Second Event",secondTry);
        utility.deleteAlarm(this,secondTry);

    }

}
