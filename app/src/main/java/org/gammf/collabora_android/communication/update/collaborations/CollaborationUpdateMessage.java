package org.gammf.collabora_android.communication.update.collaborations;

import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;

/**
 * A simple interface that represents a message containing an update operation carried out on a {@link Collaboration}.
 */
public interface CollaborationUpdateMessage extends UpdateMessage {

    /**
     * @return the updated {@link Collaboration}.
     */
    Collaboration getCollaboration();

}
