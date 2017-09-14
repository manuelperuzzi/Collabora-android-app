package org.gammf.collabora_android.communication.allCollaborations;

import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.common.AbstractMessage;
import org.gammf.collabora_android.communication.common.MessageType;
import org.gammf.collabora_android.users.User;

import java.util.Collections;
import java.util.List;

/**
 * Concrete class representing a message containing a list of {@link Collaboration}s.
 */
public class ConcreteAllCollaborationsMessage extends AbstractMessage implements AllCollaborationsMessage {

    private final List<Collaboration> collaborations;

    /**
     * Class constructor.
     * @param username tha identifier of the {@link User}.
     * @param collaborations all the {@link Collaboration}s of the {@link User}.
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
