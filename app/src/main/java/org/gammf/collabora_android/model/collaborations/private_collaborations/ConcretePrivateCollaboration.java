package org.gammf.collabora_android.model.collaborations.private_collaborations;

import org.gammf.collabora_android.model.collaborations.general.AbstractCollaboration;
import org.gammf.collabora_android.model.users.CollaborationMember;
import org.gammf.collabora_android.model.users.SimpleCollaborationMember;
import org.gammf.collabora_android.utils.model.AccessRight;
import org.gammf.collabora_android.utils.model.CollaborationType;

/**
 * A simple implementation of a {@link PrivateCollaboration}.
 */
public class ConcretePrivateCollaboration extends AbstractCollaboration implements PrivateCollaboration {

    private final CollaborationMember user;

    /**
     * Class constructor.
     * @param id the identifier of the collaboration.
     * @param name the name of the collaboration.
     * @param username the identifier of the user that owns the collaboration.
     */
    public ConcretePrivateCollaboration(final String id, final String name, final String username) {
        super(id, name);
        this.user = new SimpleCollaborationMember(username, AccessRight.ADMIN);
    }

    @Override
    public CollaborationMember getUser() {
        return user;
    }

    @Override
    public CollaborationType getCollaborationType() {
        return CollaborationType.PRIVATE;
    }
}
