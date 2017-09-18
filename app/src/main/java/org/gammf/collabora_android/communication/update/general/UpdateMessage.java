package org.gammf.collabora_android.communication.update.general;

import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;

/**
 * Simple interface which defines the contract of an message wrapping an update operation.
 */

public interface UpdateMessage extends Message {

    /**
     * @return the {@link UpdateMessageType}.
     */
    UpdateMessageType getUpdateType();

    /**
     * @return the {@link UpdateMessageTarget}.
     */
    UpdateMessageTarget getTarget();

    /**
     * @return the id of the {@link Collaboration} to which the target belongs.
     */
    String getCollaborationId();
}
