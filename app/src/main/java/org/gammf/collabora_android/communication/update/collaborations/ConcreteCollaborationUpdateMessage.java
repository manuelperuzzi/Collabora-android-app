package org.gammf.collabora_android.communication.update.collaborations;

import org.gammf.collabora_android.collaborations.Collaboration;
import org.gammf.collabora_android.communication.common.MessageType;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;

/**
 * @author ManuelPeruzzi
 * A concrete message representing an update in a collaboration.
 */
public class ConcreteCollaborationUpdateMessage implements CollaborationUpdateMessage {

    private final String username;
    private final Collaboration collaboration;
    private final UpdateMessageType updateType;

    public ConcreteCollaborationUpdateMessage(final String username, final Collaboration collaboration,
                                              final UpdateMessageType updateType) {
        this.username = username;
        this.collaboration = collaboration;
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

    @Override
    public UpdateMessageTarget getTarget() {
        return UpdateMessageTarget.COLLABORATION;
    }

    @Override
    public Collaboration getCollaboration() {
        return collaboration;
    }
    
}
