package org.gammf.collabora_android.notes;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Alfredo on 05/08/2017.
 */

public interface NoteBuilder {
    NoteBuilder setNoteID(String noteID);
    NoteBuilder setLocation(Location location);
    NoteBuilder setExpirationDate(DateTime expirationDate);
    NoteBuilder setState(State state);
    NoteBuilder setPreviousNotes(List<String> previousNotes);
    Note buildNote();
}
