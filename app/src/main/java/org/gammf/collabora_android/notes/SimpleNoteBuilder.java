package org.gammf.collabora_android.notes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Alfredo on 05/08/2017.
 */

public class SimpleNoteBuilder implements NoteBuilder {
    private final String username;
    private String noteID;
    private String content;
    private Location location;
    private Date expirationDate;
    private State state;
    private List<String> previousNotes;

    public SimpleNoteBuilder(final String username) {
        this.username = username;
    }


    @Override
    public NoteBuilder setNoteID(String noteID) {
        this.noteID = noteID;
        return this;
    }


    @Override
    public NoteBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public NoteBuilder setLocation(Location location) {
        this.location = location;
        return this;
    }


    @Override
    public NoteBuilder setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    @Override
    public NoteBuilder setState(State state) {
        this.state = state;
        return this;
    }

    @Override
    public NoteBuilder setPreviousNotes(List<String> previousNotes) {
        this.previousNotes = previousNotes;
        return this;
    }

    @Override
    public Note buildNote() {
        return new SimpleNote(this.noteID,
                              this.username,
                              this.content,
                              this.location,
                              this.expirationDate,
                              this.state,
                              previousNotes != null ? new ArrayList<>(previousNotes) : null);
    }
}
