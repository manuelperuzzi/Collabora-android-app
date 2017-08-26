package org.gammf.collabora_android.communication.update.collaborations;

import org.gammf.collabora_android.collaborations.complete_collaborations.Collaboration;
import org.gammf.collabora_android.communication.update.general.AbstractUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;

/**
 * @author ManuelPeruzzi
 * A concrete message representing an update in a collaboration.
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
