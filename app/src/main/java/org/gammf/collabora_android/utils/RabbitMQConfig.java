package org.gammf.collabora_android.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Simple class meant to contain all RabbitMQ configuration values and manage a single RabbitMQ connection.
 */

public class RabbitMQConfig {

    public static final String BROKER_ADDRESS = "192.168.1.111";
    public static final String NOTIFICATIONS_EXCHANGE_NAME = "notifications";
    public static final String UPDATES_EXCHANGE_NAME = "updates";
    public static final String COLLABORATIONS_EXCHANGE_NAME = "collaborations";
    public static final String NOTIFICATIONS_QUEUE_PREFIX = "notify.";
    public static final String COLLABORATIONS_QUEUE_PREFIX = "collaborate.";
    public static final String UPDATES_ROUTING_KEY = "";

    private static Connection connection;

    /**
     * Getter for the unique instance of the RabbitMQ connection in the application.
     * @return the RabbitMQ connection.
     * @throws IOException if something went wrong.
     * @throws TimeoutException probably if the server is unreachable.
     */
    public static synchronized Connection getRabbitMQConnection() throws IOException, TimeoutException {
        if(connection == null) {
            createConnection();
        } else if(!connection.isOpen()) {
            connection.abort();
            createConnection();
        }
        return connection;
    }

    private RabbitMQConfig() {}

    private static void createConnection() throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(BROKER_ADDRESS);
        connection = factory.newConnection();
    }
}
