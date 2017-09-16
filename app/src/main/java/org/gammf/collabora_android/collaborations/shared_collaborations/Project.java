package org.gammf.collabora_android.collaborations.shared_collaborations;

import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.collaborations.general.Collaboration;

import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Represents a {@link Collaboration} of type project.
 */
public interface Project extends SharedCollaboration {

    /**
     * @return a list containing each {@link Module} of the {@link Collaboration}.
     */
    Set<Module> getAllModules();

    /**
     * Checks if the given {@link Module} belongs to the {@link Collaboration}.
     * @param moduleId the identifier of the {@link Module}.
     * @return true if the {@link Module} belongs to the {@link Collaboration}, otherwise false.
     */
    boolean containsModule(String moduleId);

    /**
     * Returns a {@link Module} identified by its id.
     * @param moduleId the identifier of the requested {@link Module}.
     * @return the requested {@link Module}.
     * @throws NoSuchElementException if the {@link Module} does not exist.
     */
    Module getModule(String moduleId) throws NoSuchElementException;

    /**
     * Adds a {@link Module} to the {@link Collaboration}. If the {@link Module} already exist, it will be overwritten.
     * @param module the {@link Module} to be added to the {@link Collaboration}.
     * @return true if the {@link Module} is not in the {@link Collaboration}, false otherwise.
     */
    boolean addModule(Module module);

    /**
     * Removes a {@link Module} from the {@link Collaboration}.
     * @param moduleId the identifier of the {@link Module} to be removed from the {@link Collaboration}.
     * @return true if the {@link Module} was in the {@link Collaboration}, false otherwise.
     */
    boolean removeModule(String moduleId);

    /**
     * Adds a {@link Note} to the specified {@link Module} in the {@link Collaboration}.
     * @param note the {@link Note} to be added.
     * @param moduleId the identifier of the {@link Module} that will contain the {@link Note}.
     * @return true if the {@link Note} is inserted in the {@link Module}.
     */
    boolean addNote(Note note, String moduleId);

    /**
     * Method that returns all {@link Note}s that don't belong to any {@link Module},
     * @return return set of {@link Note}s.
     */
    Set<Note> getAllNoteNotInModules();



}
