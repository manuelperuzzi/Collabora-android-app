package org.gammf.collabora_android.communication.update;

import org.gammf.collabora_android.communication.common.MessageType;
import org.gammf.collabora_android.notes.Note;

/**
 * Created by Alfredo on 08/08/2017.
 */

public class ConcreteNoteUpdateMessage implements NoteUpdateMessage {
    private final String username;
    private final Note note;
    private final UpdateMessageType updateType;

    public ConcreteNoteUpdateMessage(final String username, final Note note, final UpdateMessageType updateType) {
        this.username = username;
        this.note = note;
        this.updateType = updateType;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.UPDATE;
    }

    @Override
    public UpdateMessageType getUpdateType() {
        return this.updateType;
    }

    @Override
    public UpdateMessageTarget getTarget() {
        return UpdateMessageTarget.NOTE;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Note getNote() {
        return this.note;
    }
}
