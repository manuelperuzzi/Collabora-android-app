package org.gammf.collabora_android.communication.notification;

import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.notes.Note;

/**
 * Created by Alfredo on 08/08/2017.
 */

public interface NotificationMessage extends Message{
    NotificationMessageType getNotificationType();
    Note getNote();
}
