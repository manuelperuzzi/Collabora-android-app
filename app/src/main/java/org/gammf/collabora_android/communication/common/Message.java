package org.gammf.collabora_android.communication.common;

/**
 * @author Alfredo Maffi
 * Basic interface representing a generic message of the application domain.
 */

public interface Message {
    String getUsername();
    MessageType getMessageType();
}
