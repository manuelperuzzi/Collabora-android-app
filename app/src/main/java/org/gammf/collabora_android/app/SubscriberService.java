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
import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.gammf.collabora_android.app.gui.MainActivity;
import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.notification.ConcreteNotificationMessage;
import org.gammf.collabora_android.communication.notification.NotificationMessage;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.utils.MessageUtils;
import org.gammf.collabora_android.utils.NoteUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Alfredo on 08/08/2017.
 */

public class SubscriberService extends Service {

    private static final String BROKER_ADDRESS = "192.168.1.125";
    private static final String EXCHANGE_NAME = "notifications";
    private static final String QUEUE_PREFIX = "notify.";

    private ConnectionFactory factory;
    private String queueName;
    private Thread subscriberThread;

    @Override
    public void onCreate() {
        super.onCreate();
        this.subscribe();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.queueName = QUEUE_PREFIX + intent.getStringExtra("username");
        return START_NOT_STICKY;
    }

    private void subscribe() {
        this.subscriberThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("Subscriber Thread", "setupping connection");
                    factory = new ConnectionFactory();
                    factory.setHost(BROKER_ADDRESS);
                    final Connection connection = factory.newConnection();
                    final Channel channel = connection.createChannel();
                    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
                    channel.queueDeclare(queueName, true, false, false, null);
                    channel.queueBind(queueName, EXCHANGE_NAME, "maffone");
                    channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope,
                                                   AMQP.BasicProperties properties, byte[] body) throws IOException {
                            String message = new String(body, "UTF-8");
                            try {
                                JSONObject jsn = new JSONObject(new String(body, "UTF-8"));
                                Message m = MessageUtils.jsonToUpdateMessage(jsn);
                                Note n = ((ConcreteNotificationMessage)m).getNote();
                            } catch (Exception e) {
                                Log.i("Subscriber Thread", "problem");
                                e.printStackTrace();
                            }
                            sendNotification(message);
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        }
                    });
                    Log.i("Subscriber Thread", "all ok");
                } catch(Exception e) {
                    Log.i("Subscriber Thread", "SomeThing went wrong");
                    e.printStackTrace();
                }
            }
        });
        this.subscriberThread.start();
        Log.i("Subscriber Service", "Thread started");
    }

    private void sendNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SubscriberService.this);
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Notification")
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
