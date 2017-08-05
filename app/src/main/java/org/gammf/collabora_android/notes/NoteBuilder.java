package org.gammf.collabora_android.notes;

/**
 * Created by Alfredo on 05/08/2017.
 */

public interface NoteBuilder {
    NoteBuilder setTitle(String title);
    NoteBuilder setContent(String content);
    Note buildNote();
}
