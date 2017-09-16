package org.gammf.collabora_android.model.collaborations.general;

import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.utils.model.CollaborationType;

import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Simple interface that represents a collaboration, that is a sort of cooperation where users can share {@link Note}s.
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
     * @return a list containing each {@link Note} of the collaboration.
     */
    Set<Note> getAllNotes();

    /**
     * Checks if a {@link Note} with the given noteId belongs to the collaboration.
     * @param noteId the identifier of the {@link Note}.
     * @return true if the {@link Note} belongs to the collaboration, false otherwise.
     */
    boolean containsNote(String noteId);

    /**
     * Returns a {@link Note} identified by its id.
     * @param noteId the identifier of the requested {@link Note}.
     * @return the requested {@link Note}.
     * @throws NoSuchElementException if the noteId does not exist.
     */
    Note getNote(String noteId) throws NoSuchElementException;

    /**
     * Adds a {@link Note} to the collaboration. If the {@link Note} already exist, it will be overwritten.
     * @param note the {@link Note} to be added to the collaboration.
     * @return true if the note is not in the collaboration, false otherwise.
     */
    boolean addNote(Note note);

    /**
     * Removes a {@link Note} from the collaboration.
     * @param noteId the identifier of the {@link Note} to be removed from the collaboration.
     * @return true if the {@link Note} was in the collaboration, false otherwise.
     */
    boolean removeNote(String noteId);

    /**
     * Return the {@link CollaborationType} of the collaboration.
     * @return the {@link CollaborationType} of the collaboration.
     */
    CollaborationType getCollaborationType();
}
