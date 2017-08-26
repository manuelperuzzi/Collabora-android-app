package org.gammf.collabora_android.communication.update.notes;

import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.notes.Note;

/**
 * @author Alfredo Maffi
 * A simple interface that represents a message containing an update in a note.
 */

public interface NoteUpdateMessage extends UpdateMessage {
    /**
     * @return the updated note.
     */
    Note getNote();
}
