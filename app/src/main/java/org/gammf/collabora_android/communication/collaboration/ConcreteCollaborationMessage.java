package org.gammf.collabora_android.communication.collaboration;

import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.common.MessageType;

/**
 * @author Alfredo Maffi
 * Concrete class representing a collaboration message.
 */

public class ConcreteCollaborationMessage implements CollaborationMessage {

    private final String username;
    private final MessageType messageType;
    private final Collaboration collaboration;

    public ConcreteCollaborationMessage(final String username, final MessageType messageType, final Collaboration collaboration) {
        this.username = username;
        this.messageType = messageType;
        this.collaboration = collaboration;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public MessageType getMessageType() {
        return this.messageType;
    }

    @Override
    public Collaboration getCollaboration() {
        return this.collaboration;
    }
}
