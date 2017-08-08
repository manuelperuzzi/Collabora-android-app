package org.gammf.collabora_android.communication.notification;

import org.gammf.collabora_android.communication.common.MessageType;
import org.gammf.collabora_android.notes.Note;

/**
 * Created by Alfredo on 08/08/2017.
 */

public class ConcreteNotificationMessage implements NotificationMessage{

    private final String username;
    private final Note note;
    private final NotificationMessageType notificationType;

    public ConcreteNotificationMessage(final String username, final Note note, final NotificationMessageType notificationType) {
        this.username = username;
        this.note = note;
        this.notificationType = notificationType;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.NOTIFICATION;
    }

    @Override
    public Note getNote() {
        return this.note;
    }

    @Override
    public NotificationMessageType getNotificationType() {
        return this.notificationType;
    }
}
