package org.gammf.collabora_android.communication.collaboration;

import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.common.AbstractMessage;
import org.gammf.collabora_android.communication.common.MessageType;

/**
 * Concrete class representing a {@link CollaborationMessage}.
 */

public class ConcreteCollaborationMessage extends AbstractMessage implements CollaborationMessage {

    private final Collaboration collaboration;

    public ConcreteCollaborationMessage(final String username, final Collaboration collaboration) {
        super(username, MessageType.COLLABORATION);
        this.collaboration = collaboration;
    }

    @Override
    public Collaboration getCollaboration() {
        return this.collaboration;
    }
}
