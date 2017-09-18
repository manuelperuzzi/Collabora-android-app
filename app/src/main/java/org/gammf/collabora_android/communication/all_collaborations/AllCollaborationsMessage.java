package org.gammf.collabora_android.communication.all_collaborations;

import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.common.Message;

import java.util.List;

/**
 * Represents a message containing the information about all the user {@link Collaboration}s.
 */
public interface AllCollaborationsMessage extends Message {

    /**
     * Returns all the user {@link Collaboration}s.
     */
    List<Collaboration> getCollaborationList();
}
