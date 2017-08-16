package org.gammf.collabora_android.communication.update.notes;

import org.gammf.collabora_android.communication.update.general.AbstractUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.notes.Note;

/**
 * Created by Alfredo on 08/08/2017.
 */

public class ConcreteNoteUpdateMessage extends AbstractUpdateMessage implements NoteUpdateMessage {
    private final Note note;

    public ConcreteNoteUpdateMessage(final String username, final Note note, final UpdateMessageType updateType) {
        super(username, updateType);
        this.note = note;
    }

    @Override
    public UpdateMessageTarget getTarget() {
        return UpdateMessageTarget.NOTE;
    }

    @Override
    public Note getNote() {
        return this.note;
    }
}
