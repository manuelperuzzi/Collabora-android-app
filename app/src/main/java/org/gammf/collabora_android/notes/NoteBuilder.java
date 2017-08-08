package org.gammf.collabora_android.notes;

import java.util.Date;
import java.util.List;

/**
 * Created by Alfredo on 05/08/2017.
 */

public interface NoteBuilder {
    NoteBuilder setNoteID(String noteID);
    NoteBuilder setTitle(String title);
    NoteBuilder setContent(String content);
    NoteBuilder setLatitude(Double latitude);
    NoteBuilder setLongitude(Double longitude);
    NoteBuilder setExpirationDate(Date expirationDate);
    NoteBuilder setState(String state);
    NoteBuilder setStateResponsible(String stateResponsible);
    NoteBuilder setPreviousNotes(List<String> previousNotes);
    Note buildNote();
}
