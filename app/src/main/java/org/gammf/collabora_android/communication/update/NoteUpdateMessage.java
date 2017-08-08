package org.gammf.collabora_android.communication.update;

import org.gammf.collabora_android.notes.Note;

/**
 * Created by Alfredo on 08/08/2017.
 */

public interface NoteUpdateMessage extends UpdateMessage {
    Note getNote();
}
