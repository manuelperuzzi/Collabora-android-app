package org.gammf.collabora_android.model.collaborations.private_collaborations;

import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.model.users.CollaborationMember;
import org.gammf.collabora_android.model.users.User;

/**
 * Simple interface that represents a {@link User} personal and private {@link Collaboration}.
 */
public interface PrivateCollaboration extends Collaboration {

    /**
     * @return the user that owns the private {@link Collaboration} as a {@link CollaborationMember}.
     */
    CollaborationMember getUser();

}
