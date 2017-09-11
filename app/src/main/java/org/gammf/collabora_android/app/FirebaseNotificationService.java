package org.gammf.collabora_android.app;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.gammf.collabora_android.users.User;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;

/**
 * Simple class defining a strategy to handle Firebase notifications when the application is in foreground.
 */
public class FirebaseNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        try {
            final User user = LocalStorageUtils.readUserFromFile(getApplicationContext());
            final String notificationBody = remoteMessage.getNotification().getBody();
            if (notificationBody != null && ! notificationBody.contains(user.getUsername())) {
                sendNotification(remoteMessage);
            }
        } catch (final IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(final RemoteMessage remoteMessage) {
        final RemoteMessage.Notification notification = remoteMessage.getNotification();
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true);

        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}