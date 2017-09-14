package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;

/**
 * Simple enumeration that defines the types of a {@link Collaboration}.
 */
public enum CollaborationType {

    /**
     * Unique {@link Collaboration} for personal and private {@link Note}s.
     */
    PRIVATE,

    /**
     * Simple {@link Collaboration} for everyday situations.
     */
    GROUP,

    /**
     * Elaborate collaboration for {@link Project}s.
     */
    PROJECT

}
