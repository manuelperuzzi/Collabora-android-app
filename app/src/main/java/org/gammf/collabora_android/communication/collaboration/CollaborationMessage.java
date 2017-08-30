package org.gammf.collabora_android.communication.collaboration;

import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.common.Message;

/**
 * @author Alfredo Maffi
 * Interface representing a message containing information about a collaboration.
 */

public interface CollaborationMessage extends Message{
    /**
     * Collaboration getter.
     * @return the message's collaboration.
     */
    Collaboration getCollaboration();
}
