package org.gammf.collabora_android.notes;

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
     * @return the note's state.
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
    void modifyContent(String newContent);

    /**
     * Sets a new location to the note.
     * @param location the location to be set.
     */
    void modifyLocation(Location location);

    /**
     * Sets a new expiration date to the note.
     * @param expirationDate the expiration date to be set.
     */
    void modifyExpirationDate(DateTime expirationDate);

    /**
     * Sets a new state to the note.
     * @param state the state to be set.
     */
    void modifyState(State state);

    /**
     * Sets new previous notes to the note.
     * @param previousNotes the previous notes to be set.
     */
    void modifyPreviousNotes(List<String> previousNotes);
}
