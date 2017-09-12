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
 * Class that represent Allarm management
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
        Intent intent = new Intent(context , AlarmBroadcastReceiver.class);
        intent.putExtra("title",message);
        intent.putExtra("time",timeToSpawn.getMillis());
        final long curretTime = System.currentTimeMillis();
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(getDate(timeToSpawn.getMillis(), "dd/MM/yyyy hh:mm:ss"),curretTime);
        editor.apply();

        // pendingIntend MUST have different id if we want multiple allarms to set
        final int _id = (int) curretTime;
        Log.d("DEBUG ID START", String.valueOf(_id));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _id,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(timeToSpawn.getMillis()>System.currentTimeMillis()) {
            am.set(AlarmManager.RTC_WAKEUP, timeToSpawn.getMillis(), pendingIntent);
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
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Log.d("DEBUG ID CANCEL", String.valueOf((int) prefs.getLong(getDate(timeToSpawn.getMillis(), "dd/MM/yyyy hh:mm:ss"),Long.MIN_VALUE)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) prefs.getLong(getDate(timeToSpawn.getMillis(), "dd/MM/yyyy hh:mm:ss"),Long.MIN_VALUE),intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);
    }

    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    private String getDate(long milliSeconds, String dateFormat){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ITALIAN);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


}
