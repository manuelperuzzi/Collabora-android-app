package org.gammf.collabora_android.notes;

import java.util.Date;
import java.util.List;

/**
 * Created by Alfredo on 05/08/2017.
 */

public interface Note {
    String getNoteID();
    String getUsername();
    String getTitle();
    String getContent();
    Double getLatitude();
    Double getLongitude();
    Date getExpirationDate();
    String getState();
    String getStateResponsible();
    List<String> getPreviousNotes();
    void modifyTitle(String newTitle);
    void modifyContent(String newContent);
}
