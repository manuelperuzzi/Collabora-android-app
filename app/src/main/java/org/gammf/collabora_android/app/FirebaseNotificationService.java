package org.gammf.collabora_android.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.gammf.collabora_android.app.gui.MainActivity;
import org.gammf.collabora_android.users.User;
import org.gammf.collabora_android.utils.SingletonAppUser;

/**
 * Simple class defining a strategy to handle Firebase notifications when the application is in foreground.
 */
public class FirebaseNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        final User user = SingletonAppUser.getInstance().getUser();
        final String notificationBody = remoteMessage.getNotification().getBody();
        if (notificationBody != null && ! notificationBody.contains(user.getUsername())) {
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}