package org.gammf.collabora_android.communication.update.general;

import org.gammf.collabora_android.communication.common.MessageType;

/**
 * @author Manuel Peruzzi
 * Abstract class that defines a basic behaviour of an update message.
 */
public abstract class AbstractUpdateMessage implements UpdateMessage {

    private final String username;
    private final UpdateMessageType updateType;

    protected AbstractUpdateMessage(final String username, final UpdateMessageType updateType) {
        this.username = username;
        this.updateType = updateType;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.UPDATE;
    }

    @Override
    public UpdateMessageType getUpdateType() {
        return updateType;
    }

}
