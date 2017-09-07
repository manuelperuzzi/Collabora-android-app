package org.gammf.collabora_android.communication.allCollaborations;

import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.common.Message;

import java.util.List;

/**
 * @author Manuel Peruzzi
 * Represents a message containing the information about all the user collaborations.
 */
public interface AllCollaborationsMessage extends Message {

    /**
     * Returns all the user collaborations.
     */
    List<Collaboration> getCollaborationList();
}
