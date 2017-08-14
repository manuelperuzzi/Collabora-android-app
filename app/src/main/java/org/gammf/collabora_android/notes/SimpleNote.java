package org.gammf.collabora_android.notes;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Alfredo on 05/08/2017.
 */

public class SimpleNote implements Note {
    private String noteID;
    private String content;
    private Location location;
    private DateTime expirationDate;
    private State state;
    private final List<String> previousNotes;

    SimpleNote(final String noteID,
               final String content,
               final Location location,
               final DateTime expirationDate,
               final State state,
               final List<String> previousNotes) {
        this.noteID = noteID;
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
    public String getContent() {
        return this.content;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public DateTime getExpirationDate() {
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

    @Override
    public String toString() {
        return "Simple note {" +
                (this.noteID != null ? "\n\tid: " + this.noteID : "") +
                (this.content != null ? "\n\tcontent: " + this.content : "") +
                (this.location != null ? "\n\tlocation: {latitude: " + this.location.getLatitude() + ", longitude: " + this.location.getLongitude()+ "}" : "") +
                (this.expirationDate != null ? "\n\texpiration date: " + this.expirationDate : "") +
                (this.state != null ? "\n\tstate: {current state: " + this.state.getCurrentState() + ", responsible: " + this.state.getCurrentResponsible() + "}" : "") +
                (this.getPreviousNotes() != null ? "previous notes: " + this.previousNotes : "") + "\n}";

    }
}
