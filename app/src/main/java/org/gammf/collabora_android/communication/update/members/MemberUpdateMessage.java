package org.gammf.collabora_android.communication.update.members;

import org.gammf.collabora_android.users.CollaborationMember;

/**
 * @author Manuel Peruzzi
 * A simple interface that represents a message containing an update in a collaboration about one of its members.
 */
public interface MemberUpdateMessage {

    /**
     * @return the updated member.
     */
    CollaborationMember getMember();

}
