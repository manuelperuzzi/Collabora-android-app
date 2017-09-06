package org.gammf.collabora_android.communication.allCollaborations;

import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.common.AbstractMessage;
import org.gammf.collabora_android.communication.common.MessageType;

import java.util.Collections;
import java.util.List;


public class ConcreteAllCollaborationsMessage extends AbstractMessage implements AllCollaborationsMessage {

    private final List<Collaboration> collaborations;

    public ConcreteAllCollaborationsMessage(final String username, final List<Collaboration> collaborations) {
        super(username, MessageType.ALL_COLLABORATIONS);
        this.collaborations = collaborations;
    }

    @Override
    public List<Collaboration> getCollaborationList() {
        return Collections.unmodifiableList(collaborations);
    }
}
