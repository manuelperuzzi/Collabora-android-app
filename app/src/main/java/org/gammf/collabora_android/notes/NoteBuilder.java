package org.gammf.collabora_android.notes;

import java.util.Date;
import java.util.List;

/**
 * Created by Alfredo on 05/08/2017.
 */

public interface NoteBuilder {
    NoteBuilder setNoteID(String noteID);
    NoteBuilder setContent(String content);
    NoteBuilder setLocation(Location location);
    NoteBuilder setExpirationDate(Date expirationDate);
    NoteBuilder setState(State state);
    NoteBuilder setPreviousNotes(List<String> previousNotes);
    Note buildNote();
}
