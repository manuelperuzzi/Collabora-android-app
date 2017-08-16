package org.gammf.collabora_android.app;

import android.os.AsyncTask;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.utils.MessageUtils;

/**
 * Created by Alfredo on 05/08/2017.
 */

public class SendMessageToServerTask extends AsyncTask<UpdateMessage, Void, Boolean> {

    private static final String BROKER_ADDRESS = "192.168.0.16";
    private static final String EXCHANGE_NAME = "updates";

    @Override
    protected Boolean doInBackground(UpdateMessage... messages) {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setHost(BROKER_ADDRESS);
            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
            channel.basicPublish(EXCHANGE_NAME, "", null, MessageUtils.updateMessageToJSON(messages[0]).toString().getBytes());
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
