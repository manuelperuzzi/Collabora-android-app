package org.gammf.collabora_android.app.rabbitmq;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.gammf.collabora_android.app.StoreNotificationsTask;
import org.gammf.collabora_android.utils.MessageUtils;
import org.gammf.collabora_android.utils.RabbitMQConfig;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author Alfredo Maffi
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
        new StoreNotificationsTask(getApplicationContext()).execute(MessageUtils.jsonToCollaborationMessage(message));
    }

    @Override
    protected void onConfigurationCompleted(final Intent intent) {
        try {
            this.channel.queueBind(this.queueName, RabbitMQConfig.COLLABORATIONS_EXCHANGE_NAME, intent.getStringExtra("username"));
        } catch (final IOException e) {
            //TODO
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
