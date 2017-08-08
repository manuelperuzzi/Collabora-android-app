package org.gammf.collabora_android.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * Created by Alfredo on 08/08/2017.
 */

public class SubscriberService extends Service {

    private static final String BROKER_ADDRESS = "192.168.1.125";
    private static final String EXCHANGE_NAME = "notifications";
    private static final String QUEUE_PREFIX = "notify.";

    private final ConnectionFactory factory = new ConnectionFactory();
    private String queueName;

    @Override
    public void onCreate() {
        super.onCreate();
        this.setupConnectionFactory();
        this.subscribe();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.queueName = QUEUE_PREFIX + intent.getStringExtra("username");
        return START_STICKY;
    }

    private void setupConnectionFactory() {
        try {
            factory.setAutomaticRecoveryEnabled(false);
            factory.setHost(BROKER_ADDRESS);
        } catch (final Exception e) {
            //TO-DO
        }
    }

    private void subscribe() {
        try {
            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
            channel.queueDeclare(this.queueName, true, false, false, null);
            channel.queueBind(this.queueName, EXCHANGE_NAME, "notify.collaborationID");

            channel.basicConsume(this.queueName, false, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    sendNotification(message);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            });
        } catch(Exception e) {
            //TO-DO
        }
    }

    private void sendNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SubscriberService.this);
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Publish")
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(new long[] { 100, 200, 400, 200, 400 })
                .setLights(Color.BLUE, 1000, 3000);

        final Intent targetIntent = new Intent(SubscriberService.this, MainActivity.class);
        final PendingIntent contentIntent = PendingIntent.getActivity(SubscriberService.this, 0,
                targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        final NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final int notificationID = 1;
        nManager.notify(notificationID, builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
