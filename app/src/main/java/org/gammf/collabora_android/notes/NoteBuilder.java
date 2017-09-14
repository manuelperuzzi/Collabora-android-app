package org.gammf.collabora_android.notes;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Interface defining the contract of a note builder.
 */

public interface NoteBuilder {

    /**
     * Sets the {@link Note}'s id.
     * @param noteID the id to be set.
     * @return the builder itself.
     */
    NoteBuilder setNoteID(String noteID);

    /**
     * Sets the {@link Note}'s location.
     * @param location the location to be set.
     * @return the builder itself.
     */
    NoteBuilder setLocation(Location location);

    /**
     * Sets the {@link Note}'s expiration date.
     * @param expirationDate the date to be set.
     * @return the builder itself.
     */
    NoteBuilder setExpirationDate(DateTime expirationDate);

    /**
     * Sets the {@link Note}'s previous notes.
     * @param previousNotes the previous notes to be set.
     * @return the builder itself.
     */
    NoteBuilder setPreviousNotes(List<String> previousNotes);

    /**
     * Builds a {@link Note} using the previously set information.
     * @return the brand new {@link Note}.
     */
    Note buildNote();
}
