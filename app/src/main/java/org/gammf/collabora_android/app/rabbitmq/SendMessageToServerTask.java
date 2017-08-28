package org.gammf.collabora_android.app.rabbitmq;

import android.os.AsyncTask;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.utils.MessageUtils;
import org.gammf.collabora_android.utils.RabbitMQConfig;

/**
 * @author Alfredo Maffi
 * AsyncTask meant to send a message to the server whenever the user performs a modification while using the app.
 */

public class SendMessageToServerTask extends AsyncTask<UpdateMessage, Void, Boolean> {

    /**
     * Simple usage of RabbitMQ's APIs in order to send a message to the broker.
     * @param messages array containing the message to be sent.
     * @return true if the message is sent successfully.
     */
    @Override
    protected Boolean doInBackground(UpdateMessage... messages) {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setHost(RabbitMQConfig.BROKER_ADDRESS);
            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();
            channel.exchangeDeclare(RabbitMQConfig.UPDATES_EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
            channel.basicPublish(RabbitMQConfig.UPDATES_EXCHANGE_NAME, RabbitMQConfig.UPDATES_ROUTING_KEY, null, MessageUtils.updateMessageToJSON(messages[0]).toString().getBytes());
        } catch (final Exception e) {
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //TODO integration with GUI
    }
}
