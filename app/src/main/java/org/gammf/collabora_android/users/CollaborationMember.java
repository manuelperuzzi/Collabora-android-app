package org.gammf.collabora_android.users;

import org.gammf.collabora_android.utils.AccessRight;

/**
 * @author Manuel Peruzzi
 * Represents a user member of a collaboration with a certain access right.
 */
public interface CollaborationMember extends User {

    /**
     * @return the user.
     */
    User getUser();

    /**
     * @return the access right of the user.
     */
    AccessRight getAccessRight();

}
