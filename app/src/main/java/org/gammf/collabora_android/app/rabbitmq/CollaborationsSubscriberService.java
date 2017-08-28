package org.gammf.collabora_android.app.rabbitmq;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.utils.RabbitMQConfig;

/**
 * Created by Alfredo on 28/08/2017.
 */

public class CollaborationsSubscriberService extends SubscriberService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

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
    protected void handleMessage(UpdateMessage message) {
        //TODO
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
