package org.gammf.collabora_android.app.rabbitmq;

import android.app.Service;
import android.content.Intent;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.utils.MessageUtils;
import org.gammf.collabora_android.utils.RabbitMQConfig;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author Alfredo Maffi
 * This class represents an abstract service listening for incoming messages from the server.
 */

public abstract class SubscriberService extends Service{

    protected Channel channel;
    protected String queueName;
    protected String consumerTag;

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
        this.setUpRabbitMQ(intent);
        new SubscriberThread().start();

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
            //TODO better error strategy
        }
    }

    protected abstract String getQueuePrefix();

    protected abstract String getExchangeName();

    protected abstract void handleMessage(UpdateMessage message);

    private void setUpRabbitMQ(final Intent intent) {
        this.queueName = this.getQueuePrefix() + intent.getStringExtra("username");
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMQConfig.BROKER_ADDRESS);
        try {
            final Connection connection = factory.newConnection();
            this.channel = connection.createChannel();
            this.channel.exchangeDeclare(this.getExchangeName(), BuiltinExchangeType.DIRECT, true);
            this.channel.queueDeclare(queueName, true, false, false, null);
        } catch (final Exception e) {
            //TODO better error strategy}
        }
    }

    /**
     * Thread used to register a consumer for incoming message from the server.
     * The consumer handles the received message calling the abstract method handleMessage.
     */
    private class SubscriberThread extends Thread {

        @Override
        public void run() {
            try {
                consumerTag = channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties properties, byte[] body) throws IOException {
                        try {
                            final JSONObject json = new JSONObject(new String(body, "UTF-8"));
                            final UpdateMessage message = MessageUtils.jsonToUpdateMessage(json);
                            channel.basicAck(envelope.getDeliveryTag(), false);
                            handleMessage(message);
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
