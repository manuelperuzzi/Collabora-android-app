package org.gammf.collabora_android.communication.update.members;

import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.model.users.CollaborationMember;

/**
 * A simple interface that represents a message containing an update operation carried out on a {@link CollaborationMember}.
 */
public interface MemberUpdateMessage extends UpdateMessage {

    /**
     * @return the updated {@link CollaborationMember}.
     */
    CollaborationMember getMember();

}
