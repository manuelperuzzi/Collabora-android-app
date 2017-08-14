package org.gammf.collabora_android.notes;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Alfredo on 05/08/2017.
 */

public interface Note {
    String getNoteID();
    String getContent();
    Location getLocation();
    DateTime getExpirationDate();
    State getState();
    List<String> getPreviousNotes();
    void modifyContent(String newContent);
    void modifyLocation(Location location);
    void modifyExpirationDate(DateTime expirationDate);
    void modifyState(State state);
    void modifyPreviousNotes(List<String> previousNotes);
}
