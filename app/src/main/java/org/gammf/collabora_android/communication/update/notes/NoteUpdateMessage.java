package org.gammf.collabora_android.communication.update.notes;

import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.model.notes.Note;

/**
 * * A simple interface that represents a message containing an update operation carried out on a {@link Note}.
 */

public interface NoteUpdateMessage extends UpdateMessage {
    /**
     * @return the updated {@link Note}.
     */
    Note getNote();
}
