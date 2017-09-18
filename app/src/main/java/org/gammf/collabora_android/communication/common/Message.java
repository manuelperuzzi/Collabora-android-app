package org.gammf.collabora_android.communication.common;

/**
 * Basic interface representing a generic message of the application domain.
 */

public interface Message {
    /**
     * @return the username of the user that create the message
     */
    String getUsername();

    /**
     * @return the {@link MessageType} of the message
     */
    MessageType getMessageType();
}
