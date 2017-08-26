package org.gammf.collabora_android.notes;

import org.joda.time.DateTime;

import java.util.List;

/**
 * @author Alfredo Maffi
 * Interface defining the contract of a note builder.
 */

public interface NoteBuilder {

    /**
     * Sets the note's id.
     * @param noteID the id to be set.
     * @return the builder itself.
     */
    NoteBuilder setNoteID(String noteID);

    /**
     * Sets the note's location.
     * @param location the location to be set.
     * @return the builder itself.
     */
    NoteBuilder setLocation(Location location);

    /**
     * Sets the note's expiration date.
     * @param expirationDate the date to be set.
     * @return the builder itself.
     */
    NoteBuilder setExpirationDate(DateTime expirationDate);

    /**
     * Sets the note's previous notes.
     * @param previousNotes the previous notes to be set.
     * @return the builder itself.
     */
    NoteBuilder setPreviousNotes(List<String> previousNotes);

    /**
     * Builds a Note using the previously set information.
     * @return the brand new Note.
     */
    Note buildNote();
}
