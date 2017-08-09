package org.gammf.collabora_android.notes;

import java.util.Date;
import java.util.List;

/**
 * Created by Alfredo on 05/08/2017.
 */

public interface Note {
    String getNoteID();
    String getUsername();
    String getContent();
    Location getLocation();
    Date getExpirationDate();
    State getState();
    List<String> getPreviousNotes();
    void modifyContent(String newContent);
}
