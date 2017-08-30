package org.gammf.collabora_android.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Alfredo Maffi
 * Simple class meant to contain all RabbitMQ configuration values
 */

public class RabbitMQConfig {

    public static final String BROKER_ADDRESS = "192.168.0.128";
    public static final String NOTIFICATIONS_EXCHANGE_NAME = "notifications";
    public static final String UPDATES_EXCHANGE_NAME = "updates";
    public static final String COLLABORATIONS_EXCHANGE_NAME = "collaborations";
    public static final String NOTIFICATIONS_QUEUE_PREFIX = "notify.";
    public static final String COLLABORATIONS_QUEUE_PREFIX = "collaborate.";
    public static final String UPDATES_ROUTING_KEY = "";

    private static Connection connection;

    public static Connection getRabbitMQConnection() throws IOException, TimeoutException {
        if(connection == null) {
            final ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(BROKER_ADDRESS);
            connection = factory.newConnection();
        }
        return connection;
    }

    private RabbitMQConfig() {}
}
