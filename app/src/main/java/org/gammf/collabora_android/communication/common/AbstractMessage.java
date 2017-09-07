package org.gammf.collabora_android.communication.common;

/**
 * A simple abstract class that defines the common basic operations that can be executed on a message.
 */
public class AbstractMessage implements Message {

    private final String username;
    private final MessageType messageType;

    protected AbstractMessage(final String username, final MessageType messageType) {
        this.username = username;
        this.messageType = messageType;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }
}
