package org.gammf.collabora_android.communication.update;

import org.gammf.collabora_android.communication.common.Message;

/**
 * Created by Alfredo on 08/08/2017.
 */

public interface UpdateMessage extends Message {
    UpdateMessageType getUpdateType();
    UpdateMessageTarget getTarget();
}
