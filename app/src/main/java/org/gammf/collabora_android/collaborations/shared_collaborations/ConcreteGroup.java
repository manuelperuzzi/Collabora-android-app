package org.gammf.collabora_android.collaborations.shared_collaborations;

import org.gammf.collabora_android.utils.CollaborationType;
import org.gammf.collabora_android.collaborations.general.Collaboration;

/**
 * A simple implementation of a {@link Collaboration} of type group.
 */
public class ConcreteGroup extends AbstractSharedCollaboration implements Group {

    /**
     * Class constructor.
     * @param id the identifier of the group.
     * @param name the name of the group.
     */
    public ConcreteGroup(final String id, final String name) {
        super(id, name);
    }

    @Override
    public CollaborationType getCollaborationType() {
        return CollaborationType.GROUP;
    }
}
