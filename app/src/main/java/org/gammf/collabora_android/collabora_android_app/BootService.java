package org.gammf.collabora_android.collabora_android_app;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        Utility utility = new Utility();

        Calendar firstTry = Calendar.getInstance();
        firstTry.set(Calendar.YEAR, 2017);
        firstTry.set(Calendar.MONTH, 7);
        firstTry.set(Calendar.DAY_OF_MONTH, 4);
        firstTry.set(Calendar.HOUR_OF_DAY, 9);
        firstTry.set(Calendar.MINUTE, 52);
        firstTry.set(Calendar.SECOND, 0);

        Calendar secondTry = Calendar.getInstance();
        secondTry.set(Calendar.YEAR, 2017);
        secondTry.set(Calendar.MONTH, 7);
        secondTry.set(Calendar.DAY_OF_MONTH, 4);
        secondTry.set(Calendar.HOUR_OF_DAY, 9);
        secondTry.set(Calendar.MINUTE, 53);
        secondTry.set(Calendar.SECOND, 0);

        utility.setAlarm(this,am,"First Event",firstTry);
        utility.setAlarm(this,am,"Second Event",secondTry);

    }

}
