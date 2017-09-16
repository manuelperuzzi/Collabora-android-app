package org.gammf.collabora_android.communication.update.collaborations;

import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.update.general.AbstractUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;

/**
 * A concrete message representing an update carried out on a {@link Collaboration}.
 */
public class ConcreteCollaborationUpdateMessage extends AbstractUpdateMessage
        implements CollaborationUpdateMessage {

    private final Collaboration collaboration;

    public ConcreteCollaborationUpdateMessage(final String username, final Collaboration collaboration,
                                              final UpdateMessageType updateType) {
        super(username, updateType, collaboration.getId());
        this.collaboration = collaboration;
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
