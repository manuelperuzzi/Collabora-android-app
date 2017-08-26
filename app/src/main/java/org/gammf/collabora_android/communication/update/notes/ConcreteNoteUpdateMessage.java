package org.gammf.collabora_android.communication.update.notes;

import org.gammf.collabora_android.communication.update.general.AbstractUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.notes.Note;

/**
 * @author Alfredo Maffi
 * A concrete message representing an update in a note.
 */

public class ConcreteNoteUpdateMessage extends AbstractUpdateMessage implements NoteUpdateMessage {
    private final Note note;

    public ConcreteNoteUpdateMessage(final String username, final Note note,
                                     final UpdateMessageType updateType, final String collaborationId) {
        super(username, updateType, collaborationId);
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
