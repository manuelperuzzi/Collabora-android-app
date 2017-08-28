package org.gammf.collabora_android.communication.update.collaborations;

import org.gammf.collabora_android.collaborations.complete_collaborations.SharedCollaboration;
import org.gammf.collabora_android.communication.update.general.AbstractUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;

/**
 * @author ManuelPeruzzi
 * A concrete message representing an update in a collaboration.
 */
public class ConcreteCollaborationUpdateMessage extends AbstractUpdateMessage
        implements CollaborationUpdateMessage {

    private final SharedCollaboration collaboration;

    public ConcreteCollaborationUpdateMessage(final String username, final SharedCollaboration collaboration,
                                              final UpdateMessageType updateType) {
        super(username, updateType, collaboration.getId());
        this.collaboration = collaboration;
    }

    @Override
    public UpdateMessageTarget getTarget() {
        return UpdateMessageTarget.COLLABORATION;
    }

    @Override
    public SharedCollaboration getCollaboration() {
        return collaboration;
    }
    
}
