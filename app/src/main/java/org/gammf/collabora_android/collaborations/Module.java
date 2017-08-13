package org.gammf.collabora_android.collaborations;

import org.gammf.collabora_android.notes.ModuleNote;
import org.gammf.collabora_android.notes.Note;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Manuel Peruzzi
 * Represents a module as a set of notes in a collaboration.
 */
public interface Module {

    /**
     * @return the module identifier.
     */
    String getId();

    /**
     * @return the description of the module.
     */
    String getDescription();

    /**
     * @param description the description of the module.
     */
    void setDescription(String description);

    /**
     * @return a list containing each note of the module.
     */
    List<ModuleNote> getAllNotes();

    /**
     * Returns a note identified by its id.
     * @param noteId the identifier of the requested note.
     * @return the requested note.
     * @throws NoSuchElementException if the noteId does not exist.
     */
    ModuleNote getNote(String noteId) throws NoSuchElementException;

    /**
     * Adds a note to the module. If the note already exist, it will be overwritten.
     * @param note the note to be added to the module.
     * @return true if the note is not in the module, false otherwise.
     */
    boolean addNote(Note note);

    /**
     * Removes a note from the module.
     * @param noteId the identifier of the note to be removed from the module.
     * @return true if the note was in the module, false otherwise.
     */
    boolean removeNote(String noteId);

    /**
     * @return the definition of the module state.
     */
    String getStateDefinition();

    /**
     * @param stateDefinition the definition of the module state.
     */
    void setStateDefinition(String stateDefinition);

}
