package org.gammf.collabora_android.app.rabbitmq;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import org.gammf.collabora_android.app.StoreNotificationsTask;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.all_collaborations.AllCollaborationsMessage;
import org.gammf.collabora_android.communication.collaboration.CollaborationMessage;
import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.common.MessageType;
import org.gammf.collabora_android.utils.communication.MessageUtils;
import org.gammf.collabora_android.utils.communication.RabbitMQConfig;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * This class represent a service whose task is to listen for incoming collaboration messages from the server, acting accordingly.
 */

public class CollaborationsSubscriberService extends SubscriberService {

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
        return START_REDELIVER_INTENT;
    }

    /**
     * The service, and all its components, are killed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected String getQueuePrefix() {
        return RabbitMQConfig.COLLABORATIONS_QUEUE_PREFIX;
    }


    @Override
    protected String getExchangeName() {
        return RabbitMQConfig.COLLABORATIONS_EXCHANGE_NAME;
    }

    @Override
    protected void handleJsonMessage(final JSONObject message) throws JSONException {
        final Message convertedMessage = MessageUtils.jsonToCollaborationsMessage(message);
        new StoreNotificationsTask(getApplicationContext()).execute(convertedMessage);
        if (convertedMessage.getMessageType().equals(MessageType.COLLABORATION)) {
            sendBinding(((CollaborationMessage) convertedMessage).getCollaboration().getId());
        } else if (convertedMessage.getMessageType().equals(MessageType.ALL_COLLABORATIONS)) {
            for (final Collaboration c: ((AllCollaborationsMessage) convertedMessage).getCollaborationList()) {
                sendBinding(c.getId());
            }
        }
    }

    private void sendBinding(final String routingKey) {
        final Intent intent = new Intent("new.binding.for.collaboration");
        intent.putExtra("routing-key", routingKey);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void onConfigurationCompleted(final Intent intent) {
        try {
            this.channel.queueBind(this.queueName, RabbitMQConfig.COLLABORATIONS_EXCHANGE_NAME, intent.getStringExtra("username"));
        } catch (final IOException e) {
            Toast.makeText(getApplicationContext(), "Unable to connect to server, try to restart the App!", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
