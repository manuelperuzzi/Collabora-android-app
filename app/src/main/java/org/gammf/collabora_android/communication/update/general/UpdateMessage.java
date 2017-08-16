package org.gammf.collabora_android.communication.update.general;

import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;

/**
 * Created by Alfredo on 08/08/2017.
 */

public interface UpdateMessage extends Message {
    UpdateMessageType getUpdateType();
    UpdateMessageTarget getTarget();
    String getCollaborationId();
}
