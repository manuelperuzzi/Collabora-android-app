package org.gammf.collabora_android.app.utils;

/**
 * Util class that stores all the tags used to comminicate with the main activity via Broadcast.
 */
public final class IntentConstants {

    /**
     * The tag used in intents delivered to the main activity.
     */
    public static final String MAIN_ACTIVITY_TAG = "collabora";

    /**
     * Tag used to notify a network error.
     */
    public static final String NETWORK_ERROR = "network-error";

    /**
     * Tag used to notify the receipt of a message from the server.
     */
    public static final String NETWORK_MESSAGE_RECEIVED = "network-message";

    /**
     * Tag used to aks the opening of a fragment.
     */
    public static final String OPEN_FRAGMENT = "open-fragment";

    /**
     * Tag used to notify the correct sending of a message.
     */
    public static final String MESSAGE_SENT = "message-sent";

    /**
     * Tag used to notify that a timeout occurred.
     */
    public static final String TIMEOUT = "timeout";

    public static final String COLLABORATION_DELETION = "collaboration-deletion";

    public static final String SERVER_ERROR = "server-error";

    private IntentConstants() {}
}
