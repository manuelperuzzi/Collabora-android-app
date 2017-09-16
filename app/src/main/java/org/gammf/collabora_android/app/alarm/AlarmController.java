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

import static org.gammf.collabora_android.utils.AlarmUtils.PREFS_NAME;
import org.gammf.collabora_android.users.User;

/**
 * Class that represents an alarm controller which manages all the alarms set by the {@link User}.
 */
public class AlarmController {

    /***
     * Set an alarm notification at given time, with the given information.
     * @param context the application context.
     * @param message message to show on notification.
     * @param timeToSpawn the exact time the notification need to be shown at.
     */
    public void setAlarm(Context context, String message, DateTime timeToSpawn){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context , AlarmBroadcastReceiver.class);
        intent.putExtra("title",message);
        intent.putExtra("time",timeToSpawn.getMillis());
        final long currentTime = System.currentTimeMillis();
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(getDate(timeToSpawn.getMillis(), "dd/MM/yyyy hh:mm:ss"),currentTime);
        editor.apply();
        final int _id = (int) currentTime;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _id,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(timeToSpawn.getMillis()>System.currentTimeMillis()) {
            am.set(AlarmManager.RTC_WAKEUP, timeToSpawn.getMillis(), pendingIntent);
        }
    }


    /***
     * Method used to delete a previously set alarm.
     * @param context the application context.
     * @param timeToSpawn a simpler identifier for an alarm.
     */
    public void deleteAlarm(Context context, DateTime timeToSpawn){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context , AlarmBroadcastReceiver.class);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) prefs.getLong(getDate(timeToSpawn.getMillis(), "dd/MM/yyyy hh:mm:ss"),Long.MIN_VALUE),intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);
    }

    /**
     * Returns a date in string format.
     * @param milliSeconds date expressed in milliseconds.
     * @param dateFormat the date format used to convert the date.
     * @return string representing date in the specified format.
     */
    public String getDate(long milliSeconds, String dateFormat){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ITALIAN);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


}
