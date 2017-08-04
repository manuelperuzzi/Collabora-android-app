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

    public void setAlarm(Context context, AlarmManager alarm, String message, Calendar timeToSpawn){
        Intent intent = new Intent(context , AlarmBroadcastReceiver.class);
        intent.putExtra("title",message);

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getDate(timeToSpawn.getTimeInMillis(), "dd/MM/yyyy hh:mm"), message);
        Log.d("DEBUG", getDate(timeToSpawn.getTimeInMillis(), "dd/MM/yyyy hh:mm"));
        editor.apply();

        final int _id = (int) System.currentTimeMillis();// pendingIntend MUST have different id if we want multiple allarms to set
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _id,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(timeToSpawn.getTimeInMillis()>System.currentTimeMillis()) {
            alarm.set(AlarmManager.RTC_WAKEUP, timeToSpawn.getTimeInMillis(), pendingIntent);
        }
    }

    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    private static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ITALIAN);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
