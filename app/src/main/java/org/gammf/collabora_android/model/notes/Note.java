package org.gammf.collabora_android.model.notes;

import org.joda.time.DateTime;

import java.util.List;

/**
 * @author Alfredo Maffi
 * Interface representing the contract of an application domain's note.
 */

public interface Note {

    /**
     * @return the note's id.
     */
    String getNoteID();

    /**
     * @return the note's content.
     */
    String getContent();

    /**
     * @return the note's location.
     */
    Location getLocation();

    /**
     * @return the note's expiration date.
     */
    DateTime getExpirationDate();

    /**
     * @return the note's {@link State}.
     */
    State getState();

    /**
     * @return the note's previous notes.
     */
    List<String> getPreviousNotes();

    /**
     * Sets a new content to the note.
     * @param newContent the new content to be set.
     */
    void setContent(String newContent);

    /**
     * Sets a new {@link Location} to the note.
     * @param location the location to be set.
     */
    void setLocation(Location location);

    /**
     * Sets a new expiration date to the note.
     * @param expirationDate the expiration date to be set.
     */
    void setExpirationDate(DateTime expirationDate);

    /**
     * Sets a new state to the note.
     * @param state the state to be set.
     */
    void setState(State state);

    /**
     * Sets new previous notes to the note.
     * @param previousNotes the previous notes to be set.
     */
    void setPreviousNotes(List<String> previousNotes);
}
