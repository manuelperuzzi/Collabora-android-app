package org.gammf.collabora_android.communication.update.members;

import org.gammf.collabora_android.communication.update.general.AbstractUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.model.users.CollaborationMember;

/**
 * A concrete message representing an update operation carried out on a {@link CollaborationMember}.
 */
public class ConcreteMemberUpdateMessage extends AbstractUpdateMessage implements MemberUpdateMessage {

    private final CollaborationMember member;

    public ConcreteMemberUpdateMessage(final String username, final CollaborationMember member,
                                       final UpdateMessageType updateType, final String collaborationId) {
        super(username, updateType, collaborationId);
        this.member = member;
    }

    @Override
    public UpdateMessageTarget getTarget() {
        return UpdateMessageTarget.MEMBER;
    }

    @Override
    public CollaborationMember getMember() {
        return member;
    }
}
