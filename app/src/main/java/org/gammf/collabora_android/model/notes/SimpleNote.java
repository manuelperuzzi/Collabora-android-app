package org.gammf.collabora_android.model.notes;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Concrete class representing a {@link Note} of the application domain.
 */

public class SimpleNote implements Note {
    private final String noteID;
    private String content;
    private Location location;
    private DateTime expirationDate;
    private State state;
    private List<String> previousNotes;

    /**
     * Class constructor.
     * @param noteID the note's id.
     * @param content the note's content.
     * @param location the note's location.
     * @param expirationDate the note's expiration date.
     * @param state the note's state.
     * @param previousNotes the note's previous notes.
     */
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
    public void setContent(String newContent) {
        this.content = newContent;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void setPreviousNotes(List<String> previousNotes) {
        this.previousNotes = previousNotes;
    }

    @Override
    public String toString() {
        return "Simple note {" +
                (this.noteID != null ? "\n\tid: " + this.noteID : "") +
                (this.content != null ? "\n\tcontent: " + this.content : "") +
                (this.location != null ? "\n\tlocation: {latitude: " + this.location.getLatitude() + ", longitude: " + this.location.getLongitude()+ "}" : "") +
                (this.expirationDate != null ? "\n\texpiration date: " + this.expirationDate : "") +
                (this.state != null ? "\n\tstate: {current state: " + this.state.getCurrentDefinition() + ", responsible: " + this.state.getCurrentResponsible() + "}" : "") +
                (this.getPreviousNotes() != null ? "previous notes: " + this.previousNotes : "") + "\n}";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleNote that = (SimpleNote) o;

        return noteID != null ? noteID.equals(that.noteID) : that.noteID == null;

    }

    @Override
    public int hashCode() {
        return noteID != null ? noteID.hashCode() : 0;
    }
}
