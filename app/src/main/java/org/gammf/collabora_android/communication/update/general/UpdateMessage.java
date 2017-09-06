package org.gammf.collabora_android.communication.update.general;

import org.gammf.collabora_android.communication.common.Message;

/**
 * @author Alfredo Maffi
 * Simple interface which defines the contract of an UpdateMessage.
 */

public interface UpdateMessage extends Message {

    /**
     * @return the UpdateMessage type.
     */
    UpdateMessageType getUpdateType();

    /**
     * @return the UpdateMessage target.
     */
    UpdateMessageTarget getTarget();

    /**
     * @return the id of the collaboration to which the target belongs.
     */
    String getCollaborationId();
}
