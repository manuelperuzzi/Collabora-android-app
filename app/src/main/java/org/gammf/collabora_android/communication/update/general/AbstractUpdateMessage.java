package org.gammf.collabora_android.communication.update.general;

import org.gammf.collabora_android.communication.common.AbstractMessage;
import org.gammf.collabora_android.communication.common.MessageType;

/**
 * @author Manuel Peruzzi
 * Abstract class that defines a basic behaviour of an update message.
 */
public abstract class AbstractUpdateMessage extends AbstractMessage implements UpdateMessage {

    private final UpdateMessageType updateType;
    private final String collaborationId;

    protected AbstractUpdateMessage(final String username, final UpdateMessageType updateType, final String collaborationId) {
        super(username, MessageType.UPDATE);
        this.updateType = updateType;
        this.collaborationId = collaborationId;
    }

    @Override
    public UpdateMessageType getUpdateType() {
        return updateType;
    }

    @Override
    public String getCollaborationId() {
        return collaborationId;
    }

}
