package org.gammf.collabora_android.app.rabbitmq;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.gammf.collabora_android.app.StoreNotificationsTask;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.utils.MessageUtils;
import org.gammf.collabora_android.utils.RabbitMQConfig;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * @author Alfredo Maffi
 * This class represent a service whose task is to listen for incoming notifications from the server, acting accordingly.
 */

public class SubscriberService extends Service {

    private Channel channel;
    private String queueName;
    private BroadcastReceiver createBindingReceiver;
    private BroadcastReceiver destroyBindingReceiver;

    /**
     * Registering receivers for binding/unbinding the queue from the exchange
     */
    @Override
    public void onCreate() {
        super.onCreate();
        this.createBindingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                createBinding(intent.getStringExtra("routing-key"));
            }
        };
        this.destroyBindingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    channel.queueUnbind(queueName, RabbitMQConfig.NOTIFICATIONS_EXCHANGE_NAME, intent.getStringExtra("routing-key"));
                } catch (IOException e) {
                    //TODO better error strategy
                }
            }
        };
    }

    /**
     * When the service is started, the user's queue is declared and created (if it doesn't exist).
     * Also, a basic consumer is registered on such queue and all existing bindings are established.
     * @param intent contains the username that will be the suffix of the queue's name
     * @param flags not used
     * @param startId not used
     * @return constant START_REDELIVER_INTENT in order to force the system to schedule the recreation of the service if it's killed during its creation, redelivering the same intent
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.queueName = RabbitMQConfig.NOTIFICATIONS_QUEUE_PREFIX + intent.getStringExtra("username");
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMQConfig.BROKER_ADDRESS);
        try {
            final Connection connection = factory.newConnection();
            this.channel = connection.createChannel();
            this.channel.exchangeDeclare(RabbitMQConfig.NOTIFICATIONS_EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
            this.channel.queueDeclare(queueName, true, false, false, null);

            new SubscriberThread().start();

            List<String> collaborationsIds = intent.getStringArrayListExtra("collaborationsIds");
            if(collaborationsIds != null) {
                for (final String id : collaborationsIds) {
                    this.createBinding(id);
                }
            }
        } catch (final Exception e) {
            //TODO better error strategy}
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(createBindingReceiver, new IntentFilter("new.binding.for.collaboration"));
        LocalBroadcastManager.getInstance(this).registerReceiver(destroyBindingReceiver, new IntentFilter("remove.binding.for.collaboration"));

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(createBindingReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(destroyBindingReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createBinding(final String routingKey) {
        try {
            channel.queueBind(queueName, RabbitMQConfig.NOTIFICATIONS_EXCHANGE_NAME, RabbitMQConfig.NOTIFICATIONS_QUEUE_PREFIX + routingKey);
        } catch (IOException e) {
            //TODO better error strategy
            e.printStackTrace();
        }
    }

    /**
     * Thread used to register a consumer for incoming message from the server.
     * The consumer registered is responsible for storing updates to local data.
     */
    private class SubscriberThread extends Thread {

        @Override
        public void run() {
            try {
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
            } catch (final Exception e) {
                //TODO better error strategy
            }
        }
    }
}
