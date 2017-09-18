package org.gammf.collabora_android.model.short_collaborations;

import org.gammf.collabora_android.utils.model.CollaborationType;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;

/**
 * Represents a {@link Collaboration} containing only its descriptive state.
 */
public interface ShortCollaboration {

    /**
     * @return the {@link Collaboration} identifier.
     */
    String getId();

    /**
     * @return the name of the {@link Collaboration}.
     */
    String getName();

    /**
     * @return the {@link CollaborationType} of the {@link Collaboration}.
     */
    CollaborationType getCollaborationType();

}
