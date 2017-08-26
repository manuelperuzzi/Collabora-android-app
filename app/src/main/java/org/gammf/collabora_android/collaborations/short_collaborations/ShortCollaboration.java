package org.gammf.collabora_android.collaborations.short_collaborations;

import org.gammf.collabora_android.collaborations.CollaborationType;

/**
 * @author Manuel Peruzzi
 * Represents a collaboration containing only its descriptive state.
 */
public interface ShortCollaboration {

    /**
     * @return the collaboration identifier.
     */
    String getId();

    /**
     * @return the name of the collaboration.
     */
    String getName();

    /**
     * @return the type of the collaboration.
     */
    CollaborationType getCollaborationType();

}
