package org.gammf.collabora_android.app.rabbitmq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.gammf.collabora_android.app.StoreNotificationsTask;
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

public class NotificationsSubscriberService extends SubscriberService {

    private BroadcastReceiver createBindingReceiver;
    private BroadcastReceiver destroyBindingReceiver;

    /**
     * Registering receivers for binding/unbinding the queue from the exchange.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        this.createBindingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                createBinding(intent.getStringExtra("routing-key"));
                FirebaseMessaging.getInstance().subscribeToTopic(intent.getStringExtra("routing-key"));
            }
        };
        this.destroyBindingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    channel.queueUnbind(queueName, RabbitMQConfig.NOTIFICATIONS_EXCHANGE_NAME, intent.getStringExtra("routing-key"));
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(intent.getStringExtra("routing-key"));
                } catch (final Exception e) {
                    //TODO better error strategy
                }
            }
        };
    }

    /**
     * Method used to establish all the necessary bindings to receive the server's notifications.
     * Also, this method register two BroadcastReceiver in order to manage the creation of new bindings.
     * @param intent contains the username that will be the suffix of the queue's name and, potentially, all the collaborations IDs to which the user has joined to.
     * @param flags  unused
     * @param startId unused
     * @return constant START_REDELIVER_INTENT in order to force the system to schedule the recreation of the service if it's killed during its creation, redelivering the same intent.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        LocalBroadcastManager.getInstance(this).registerReceiver(createBindingReceiver, new IntentFilter("new.binding.for.collaboration"));
        LocalBroadcastManager.getInstance(this).registerReceiver(destroyBindingReceiver, new IntentFilter("remove.binding.for.collaboration"));

        return START_REDELIVER_INTENT;
    }

    /**
     * The service, and all its components, are killed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(createBindingReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(destroyBindingReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected String getQueuePrefix() {
        return RabbitMQConfig.NOTIFICATIONS_QUEUE_PREFIX;
    }

    @Override
    protected String getExchangeName() {
        return RabbitMQConfig.NOTIFICATIONS_EXCHANGE_NAME;
    }

    @Override
    protected void handleJsonMessage(final JSONObject message) throws JSONException {
        new StoreNotificationsTask(getApplicationContext()).execute(MessageUtils.jsonToUpdateMessage(message));
    }

    @Override
    protected void onConfigurationCompleted(final Intent intent) {
        final List<String> collaborationsIds = intent.getStringArrayListExtra("collaborationsIds");
        if(collaborationsIds != null) {
            for (final String id : collaborationsIds) {
                this.createBinding(id);
                FirebaseMessaging.getInstance().subscribeToTopic(id);
            }
        }
    }

    private void createBinding(final String routingKey) {
        try {
            channel.queueBind(queueName, RabbitMQConfig.NOTIFICATIONS_EXCHANGE_NAME, routingKey);
        } catch (final IOException e) {
            Toast.makeText(getApplicationContext(), "Unable to connect to server, try to restart the App!", Toast.LENGTH_SHORT).show();
        }
    }
}
