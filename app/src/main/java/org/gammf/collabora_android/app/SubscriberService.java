package org.gammf.collabora_android.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.gammf.collabora_android.app.gui.MainActivity;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.utils.CollaboraAppUtils;
import org.gammf.collabora_android.utils.MessageUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author Alfredo Maffi
 * This class represent a service whose task is to listen for incoming notifications from the server, acting accordingly.
 */

public class SubscriberService extends Service {

    /**
     * rabbitMQ constants
     */
    private static final String BROKER_ADDRESS = "192.168.1.125";
    private static final String EXCHANGE_NAME = "notifications";
    private static final String QUEUE_PREFIX = "notify.";

    private Channel channel;
    private String queueName;
    private BroadcastReceiver binderReceiver;
    private BroadcastReceiver unbinderReceiver;
    private boolean isConsumerRegistered;

    @Override
    public void onCreate() {
        super.onCreate();
        this.isConsumerRegistered = false;
        this.binderReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String collaborationID = intent.getStringExtra("routing-key");
                new SubscriberThread(collaborationID).start();
            }
        };
        this.unbinderReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    channel.queueUnbind(queueName, EXCHANGE_NAME, intent.getStringExtra("routing-key"));
                } catch (IOException e) {
                    //TODO better error strategy
                }
            }
        };
    }

    /**
     * When the service is started, the user's queue is declared and created (if it doesn't exist)
     * @param intent contains the username that will be the suffix of the queue's name
     * @param flags not used
     * @param startId not used
     * @return constant START_REDELIVER_INTENT in order to force the system to schedule the recreation of the service if it's killed during its creation, redelivering the same intent
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.queueName = QUEUE_PREFIX + intent.getStringExtra("username");
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(BROKER_ADDRESS);
        try {
            final Connection connection = factory.newConnection();
            this.channel = connection.createChannel();
            this.channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
            this.channel.queueDeclare(queueName, true, false, false, null);
        } catch (final Exception e) {
            //TODO better error strategy}
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(binderReceiver, new IntentFilter("new.binding.for.collaboration"));
        LocalBroadcastManager.getInstance(this).registerReceiver(unbinderReceiver, new IntentFilter("remove.binding.for.collaboration"));

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(binderReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(unbinderReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Thread used to create bindings between the user's queue and the exchange notifications
     * It is also used to register a consumer for incoming message when the first binding is established
     * The consumer registered is able to show notifications to the user
     */
    private class SubscriberThread extends Thread {

        private final String collaborationID;

        private SubscriberThread(final String collaborationID) {
            this.collaborationID = collaborationID;
        }

        @Override
        public void run() {
            try {
                channel.queueBind(queueName, EXCHANGE_NAME, QUEUE_PREFIX + collaborationID);
                if(!isConsumerRegistered) {
                    channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope,
                                                   AMQP.BasicProperties properties, byte[] body) throws IOException {
                            try {
                                final JSONObject json = new JSONObject(new String(body, "UTF-8"));
                                final UpdateMessage message = MessageUtils.jsonToUpdateMessage(json);
                                channel.basicAck(envelope.getDeliveryTag(), false);
                                new StoreNotificationsTask(getApplicationContext()).execute(message);
                            } catch (final JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    isConsumerRegistered = true;
                }
            } catch (final Exception e) {
                //TODO better error strategy
            }
        }

        /*private void buildNotification(String message) {
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(SubscriberService.this);
            try {
                final UpdateMessage updateMessage = MessageUtils.jsonToUpdateMessage(new JSONObject(message));
                builder.setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Notification")
                        .setContentText(CollaboraAppUtils.getReadableNotificationMessage(updateMessage))
                        .setAutoCancel(true)
                        .setVibrate(new long[] { 100, 200, 400, 200, 400 })
                        .setLights(Color.BLUE, 1000, 3000);
                sendNotification(builder);
            } catch (final JSONException e) {
                //hypothetically unreachable
            }
        }

        private void sendNotification(NotificationCompat.Builder builder) {
            final Intent targetIntent = new Intent(SubscriberService.this, MainActivity.class);
            final PendingIntent contentIntent = PendingIntent.getActivity(SubscriberService.this, 0,
                    targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            final NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            final int notificationID = 1;
            nManager.notify(notificationID, builder.build());
        }*/
    }
}
