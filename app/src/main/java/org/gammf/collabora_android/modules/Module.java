package org.gammf.collabora_android.modules;

import org.gammf.collabora_android.notes.ModuleNote;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.collaborations.general.Collaboration;

import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Represents a module as a set of {@link Note}s in a {@link Collaboration}.
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
    Set<ModuleNote> getAllNotes();

    /**
     * Checks if the module contains a certain {@link Note}.
     * @param noteId the identifier of the {@link Note}.
     * @return true if the {@link Note} is in the module, false otherwise.
     */
    boolean containsNote(String noteId);

    /**
     * Returns a {@link Note} identified by its id.
     * @param noteId the identifier of the requested {@link Note}.
     * @return the requested {@link Note}.
     * @throws NoSuchElementException if the {@link Note} with the given noteID does not exist.
     */
    ModuleNote getNote(String noteId) throws NoSuchElementException;

    /**
     * Adds a {@link Note} to the module. If the {@link Note} already exist, it will be overwritten.
     * @param note the {@link Note} to be added to the module.
     * @return true if the {@link Note} is not in the module, false otherwise.
     */
    boolean addNote(Note note);

    /**
     * Removes a {@link Note} from the module.
     * @param noteId the identifier of the {@link Note} to be removed from the module.
     * @return true if the {@link Note} was in the module, false otherwise.
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
