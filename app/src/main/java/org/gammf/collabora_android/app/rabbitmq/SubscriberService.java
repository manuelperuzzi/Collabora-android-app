package org.gammf.collabora_android.app.rabbitmq;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.gammf.collabora_android.app.gui.MainActivity;
import org.gammf.collabora_android.app.utils.IntentConstants;
import org.gammf.collabora_android.utils.communication.RabbitMQConfig;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * This class represents an abstract service listening for incoming messages from the server.
 */

public abstract class SubscriberService extends Service{

    private BroadcastReceiver deletionReceiver;

    protected Channel channel;
    protected String queueName;
    protected String consumerTag;

    public void onCreate() {
        this.deletionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    channel.queueDelete(queueName);
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * In this method, RabbitMQ configuration is performed and a basic consumer is registered on the user's queue.
     * @param intent contains the username that will be the suffix of the queue's name
     * @param flags unused
     * @param startId unused
     * @return constant START_REDELIVER_INTENT in order to force the system to schedule the recreation of the service if it's killed during its creation, redelivering the same intent
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        LocalBroadcastManager.getInstance(this).registerReceiver(deletionReceiver, new IntentFilter("subscriber.service.deletion"));
        new SubscriberThread(intent).start();

        return START_REDELIVER_INTENT;
    }

    /**
     * The service, and all its components, are killed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            this.channel.basicCancel(this.consumerTag);
            this.channel.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract String getQueuePrefix();

    protected abstract String getExchangeName();

    protected abstract void handleJsonMessage(final JSONObject message) throws JSONException;

    protected abstract void onConfigurationCompleted(final Intent intent);

    /**
     * Thread used to register a consumer for incoming message from the server.
     * The consumer handles the received message calling the abstract method handleMessage.
     */
    private class SubscriberThread extends Thread {
        private final Intent intent;

        public SubscriberThread(final Intent intent) {
            this.intent = intent;
        }

        @Override
        public void run() {
            try {
                queueName = getQueuePrefix() + intent.getStringExtra("username");
                channel = RabbitMQConfig.getRabbitMQConnection().createChannel();
                channel.exchangeDeclare(getExchangeName(), BuiltinExchangeType.DIRECT, true);
                channel.queueDeclare(queueName, true, false, false, null);
                consumerTag = channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties properties, byte[] body) throws IOException {
                        try {
                            final JSONObject json = new JSONObject(new String(body, "UTF-8"));
                            channel.basicAck(envelope.getDeliveryTag(), false);
                            handleJsonMessage(json);
                        } catch (final JSONException e) {
                            showNetworkErrorToast();
                        }
                    }
                });
                onConfigurationCompleted(this.intent);
            } catch (final Exception e) {
                showNetworkErrorToast();
            }
        }
    }

    private void showNetworkErrorToast() {
        final Intent intent = new Intent(MainActivity.getReceiverIntentFilter());
        intent.putExtra(IntentConstants.MAIN_ACTIVITY_TAG, IntentConstants.NETWORK_ERROR);
        intent.putExtra(IntentConstants.NETWORK_ERROR, "Network Error");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
