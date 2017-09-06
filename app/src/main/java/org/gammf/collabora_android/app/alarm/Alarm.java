package org.gammf.collabora_android.app.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static org.gammf.collabora_android.utils.AlarmAndGeofenceUtils.PREFS_NAME;

/**
 * Created by Federico on 04/08/2017.
 */

public class Alarm {

    /***
     * Set an alarm notification at given time, with the given information.
     * @param context Activity when set the alarm
     * @param message message to show on notification
     * @param timeToSpawn exact time when spawn the notification
     */
    public void setAlarm(Context context, String message, DateTime timeToSpawn){

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(timeToSpawn.getYear(), timeToSpawn.getMonthOfYear(), timeToSpawn.getDayOfMonth(),
                timeToSpawn.getHourOfDay(), timeToSpawn.getMinuteOfHour(), timeToSpawn.getSecondOfMinute());

        Intent intent = new Intent(context , AlarmBroadcastReceiver.class);
        intent.putExtra("title",message);
        intent.putExtra("time",calendar.getTimeInMillis());

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(getDate(calendar.getTimeInMillis(), "dd/MM/yyyy hh:mm:ss"),System.currentTimeMillis());
        editor.apply();

        // pendingIntend MUST have different id if we want multiple allarms to set
        final int _id = (int) System.currentTimeMillis();
        Log.d("DEBUG ID START", String.valueOf(_id));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _id,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(calendar.getTimeInMillis()>System.currentTimeMillis()) {
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }


    /***
     * method to delete an active alarm
     * @param context Activity when call this method
     * @param timeToSpawn A simpler identifier for an alarm, TO TEST, maybe to change!!
     */
    public void deleteAlarm(Context context, DateTime timeToSpawn){

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context , AlarmBroadcastReceiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(timeToSpawn.getYear(), timeToSpawn.getMonthOfYear(), timeToSpawn.getDayOfMonth(),
                timeToSpawn.getHourOfDay(), timeToSpawn.getMinuteOfHour(), timeToSpawn.getSecondOfMinute());
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Log.d("DEBUG ID CANCEL", String.valueOf((int) prefs.getLong(getDate(calendar.getTimeInMillis(), "dd/MM/yyyy hh:mm:ss"),Long.MIN_VALUE)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) prefs.getLong(getDate(calendar.getTimeInMillis(), "dd/MM/yyyy hh:mm:ss"),Long.MIN_VALUE),intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);

    }

    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    public String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ITALIAN);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


}
