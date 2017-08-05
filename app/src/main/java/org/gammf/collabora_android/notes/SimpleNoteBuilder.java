package org.gammf.collabora_android.notes;

/**
 * Created by Alfredo on 05/08/2017.
 */

public class SimpleNoteBuilder implements NoteBuilder {
    private final String userID;
    private String title;
    private String content;

    public SimpleNoteBuilder(final String userID) {
        this.userID = userID;
    }


    @Override
    public NoteBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public NoteBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public Note buildNote() {
        return new SimpleNote(this.userID, this.title, this.content);
    }
}
