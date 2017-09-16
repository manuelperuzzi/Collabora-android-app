package org.gammf.collabora_android.communication.collaboration;

import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.common.Message;

/**
 * Interface representing a message containing information about a {@link Collaboration}.
 */

public interface CollaborationMessage extends Message{
    /**
     * Collaboration getter.
     * @return the message's {@link Collaboration}.
     */
    Collaboration getCollaboration();
}
