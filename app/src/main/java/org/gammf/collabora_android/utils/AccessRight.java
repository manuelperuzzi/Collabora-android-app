package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.users.User;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.notes.Note;

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
