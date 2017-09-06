package org.gammf.collabora_android.collaborations.general;

import org.gammf.collabora_android.notes.Note;

import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Manuel Peruzzi
 * Simple interface that represents a collaboration.
 */
public interface Collaboration {

    /**
     * @return the collaboration identifier.
     */
    String getId();

    /**
     * @return the name of the collaboration.
     */
    String getName();

    /**
     * @param name the name of the collaboration.
     */
    void setName(String name);

    /**
     * @return a list containing each note of the collaboration.
     */
    Set<Note> getAllNotes();

    /**
     * Checks if the given note belongs to the collaboration.
     * @param noteId the identifier of the note.
     * @return true if the note belongs to the collaboration, false otherwise.
     */
    boolean containsNote(String noteId);

    /**
     * Returns a note identified by its id.
     * @param noteId the identifier of the requested note.
     * @return the requested note.
     * @throws NoSuchElementException if the noteId does not exist.
     */
    Note getNote(String noteId) throws NoSuchElementException;

    /**
     * Adds a note to the collaboration. If the note already exist, it will be overwritten.
     * @param note the note to be added to the collaboration.
     * @return true if the note is not in the collaboration, false otherwise.
     */
    boolean addNote(Note note);

    /**
     * Removes a note from the collaboration.
     * @param noteId the identifier of the note to be removed from the collaboration.
     * @return true if the note was in the collaboration, false otherwise.
     */
    boolean removeNote(String noteId);

}
