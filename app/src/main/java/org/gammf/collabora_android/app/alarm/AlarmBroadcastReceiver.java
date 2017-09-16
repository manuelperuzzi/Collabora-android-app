package org.gammf.collabora_android.app.alarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.gammf.collabora_android.app.R;
import java.util.Date;


/**
 * {@link BroadcastReceiver} that receives notifications when an alarm is fired by the system.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmController alarmController = new AlarmController();
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Note expired! " + alarmController.getDate(intent.getExtras().getLong("time"), "dd/MM/yyyy hh:mm"))
                .setContentText(intent.getExtras().getString("title"))
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(when)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify( (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mNotifyBuilder.build());
    }

}
