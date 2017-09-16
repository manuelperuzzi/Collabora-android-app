package org.gammf.collabora_android.utils.model;

import org.gammf.collabora_android.model.users.User;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.model.users.CollaborationMember;
import org.gammf.collabora_android.model.notes.Note;

/**
 * Defines the level of access right of a {@link User} in a collaboration.
 */
public enum AccessRight {

    /**
     * Includes write privilege, can alter the whole {@link Collaboration} and its {@link CollaborationMember}s.
     */
    ADMIN,

    /**
     * Can read and modify the {@link Note}s in the {@link Collaboration}.
     */
    WRITE,

    /**
     * Can only read the {@link Note}s in the {@link Collaboration}.
     */
    READ

}
