package org.gammf.collabora_android.collabora_android_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.SystemUpdateInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Federico on 03/08/2017.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    public static final String PREFS_NAME = "CollaboraPrefs";

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Log.d("DEBUG", getDate(System.currentTimeMillis(), "dd/MM/yyyy hh:mm"));

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.ic_launcher)
                //.setContentTitle(intent.getExtras().getString("title"))
                .setContentTitle(prefs.getString(getDate(System.currentTimeMillis(), "dd/MM/yyyy hh:mm"),"ERRORONE"))
                .setContentText("Events To be Performed")
                .setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify( (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mNotifyBuilder.build());

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
