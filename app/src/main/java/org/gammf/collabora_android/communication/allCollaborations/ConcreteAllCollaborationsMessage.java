package org.gammf.collabora_android.communication.allCollaborations;

import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.common.AbstractMessage;
import org.gammf.collabora_android.communication.common.MessageType;

import java.util.Collections;
import java.util.List;

/**
 * @author Manuel Peruzzi
 * Concrete class representing a message containing a list of collaborations.
 */
public class ConcreteAllCollaborationsMessage extends AbstractMessage implements AllCollaborationsMessage {

    private final List<Collaboration> collaborations;

    /**
     * Class constructor.
     * @param username tha identifier of the user.
     * @param collaborations all the collaborations of the user.
     */
    public ConcreteAllCollaborationsMessage(final String username, final List<Collaboration> collaborations) {
        super(username, MessageType.ALL_COLLABORATIONS);
        this.collaborations = collaborations;
    }

    @Override
    public List<Collaboration> getCollaborationList() {
        return Collections.unmodifiableList(collaborations);
    }
}
