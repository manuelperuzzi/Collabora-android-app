package org.gammf.collabora_android.notes;

/**
 * Created by Alfredo on 05/08/2017.
 */

public interface Note {
    String getUserID();
    String getTitle();
    String getContent();
    void modifyTitle(String newTitle);
    void modifyContent(String newContent);
}
