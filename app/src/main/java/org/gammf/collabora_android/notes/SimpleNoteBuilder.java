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
    private String title;
    private String content;
    private Double latitude;
    private Double longitude;
    private Date expirationDate;
    private String state;
    private String stateResponsible;
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
    public NoteBuilder setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    @Override
    public NoteBuilder setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    @Override
    public NoteBuilder setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    @Override
    public NoteBuilder setState(String state) {
        this.state = state;
        return this;
    }

    @Override
    public NoteBuilder setStateResponsible(String stateResponsible) {
        this.stateResponsible = stateResponsible;
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
                              this.title,
                              this.content,
                              this.latitude,
                              this.longitude,
                              this.expirationDate,
                              this.state,
                              this.stateResponsible,
                              previousNotes != null ? new ArrayList<>(previousNotes) : null);
    }
}
