package org.gammf.collabora_android.app.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import org.gammf.collabora_android.app.gui.MainActivity;
import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.utils.IntentConstants;

import java.util.Date;

/**
 * Created by Federico on 03/08/2017.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Alarm alarm = new Alarm();
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(intent.getExtras().getString("title")+ " -- "+ alarm.getDate(intent.getExtras().getLong("time"), "dd/MM/yyyy hh:mm"))
                .setContentText("Click notification to return to app")
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify( (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mNotifyBuilder.build());
    }

}
