package org.gammf.collabora_android.communication.update.collaborations;

import org.gammf.collabora_android.collaborations.complete_collaborations.Collaboration;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;

/**
 * @author Manuel Peruzzi
 * A simple interface that represents a message containing an update in a collaboration.
 */
public interface CollaborationUpdateMessage extends UpdateMessage {

    /**
     * @return the updated collaboration.
     */
    Collaboration getCollaboration();

}
