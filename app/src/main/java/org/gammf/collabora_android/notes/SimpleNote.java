package org.gammf.collabora_android.notes;

import java.util.Date;
import java.util.List;

/**
 * Created by Alfredo on 05/08/2017.
 */

public class SimpleNote implements Note {
    private String noteID;
    private String username;
    private String content;
    private Location location;
    private Date expirationDate;
    private State state;
    private final List<String> previousNotes;

    SimpleNote(final String noteID,
               final String username,
               final String content,
               final Location location,
               final Date expirationDate,
               final State state,
               final List<String> previousNotes) {
        this.noteID = noteID;
        this.username = username;
        this.content = content;
        this.location = location;
        this.expirationDate = expirationDate;
        this.state = state;
        this.previousNotes = previousNotes;
    }


    @Override
    public String getNoteID() {
        return this.noteID;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Date getExpirationDate() {
        return this.expirationDate;
    }

    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public List<String> getPreviousNotes() {
        return this.previousNotes;
    }

    @Override
    public void modifyContent(String newContent) {
        this.content = newContent;
    }
}
