package org.gammf.collabora_android.collaborations.private_collaborations;

import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.users.CollaborationMember;

/**
 * @author Manuel Peruzzi
 * Simple interface that represents a user personal and private collaboration.
 */
public interface PrivateCollaboration extends Collaboration {

    /**
     * @return the user that owns the private collaboration.
     */
    CollaborationMember getUser();

}
