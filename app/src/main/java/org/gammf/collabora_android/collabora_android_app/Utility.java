package org.gammf.collabora_android.collabora_android_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Federico on 04/08/2017.
 */

public class Utility {

    public static final String PREFS_NAME = "CollaboraPrefs";

    /***
     * Set an alarm notification at given time, with the given information.
     * @param context Activity when set the alarm
     * @param message message to show on notification
     * @param timeToSpawn exact time when spawn the notification
     */
    public void setAlarm(Context context, String message, Calendar timeToSpawn){

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context , AlarmBroadcastReceiver.class);

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getDate(timeToSpawn.getTimeInMillis(), "dd/MM/yyyy hh:mm"), message);
        editor.apply();

        // pendingIntend MUST have different id if we want multiple allarms to set
        final int _id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _id,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(timeToSpawn.getTimeInMillis()>System.currentTimeMillis()) {
            am.set(AlarmManager.RTC_WAKEUP, timeToSpawn.getTimeInMillis(), pendingIntent);
        }
    }

    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ITALIAN);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
