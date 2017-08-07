package org.gammf.collabora_android.collabora_android_app.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.gammf.collabora_android.collabora_android_app.MainActivity;
import org.gammf.collabora_android.collabora_android_app.R;
import org.gammf.collabora_android.collabora_android_app.Utility;

import java.util.Date;

/**
 * Created by Federico on 03/08/2017.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    //public static final String PREFS_NAME = "CollaboraPrefs";

    @Override
    public void onReceive(Context context, Intent intent) {

        Utility utility = new Utility();
        //SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(intent.getExtras().getString("title")+ " -- "+ utility.getDate(intent.getExtras().getLong("time"), "dd/MM/yyyy hh:mm"))
                .setContentText("Insert here Note content")
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify( (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mNotifyBuilder.build());

    }

}
