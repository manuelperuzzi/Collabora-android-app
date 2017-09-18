package org.gammf.collabora_android.model.short_collaborations;

import org.gammf.collabora_android.utils.model.CollaborationType;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Represents a manager that handles all the {@link Collaboration}s of the application, in their short format.
 */
public interface CollaborationsManager {

    /**
     * @return a set containing each {@link Collaboration}.
     */
    Set<ShortCollaboration> getAllCollaborations();

    /**
     * Checks a certain {@link Collaboration} is present.
     * @param collaborationId the identifier of the {@link Collaboration}.
     * @return true if the {@link Collaboration} is in the manager, false otherwise.
     */
    boolean containsCollaboration(String collaborationId);

    /**
     * Returns a {@link ShortCollaboration} identified by its id.
     * @param collaborationId the identifier of the requested {@link ShortCollaboration}.
     * @return the requested {@link ShortCollaboration}.
     * @throws NoSuchElementException if the {@link ShortCollaboration} id does not exist in the manager.
     */
    ShortCollaboration getCollaboration(String collaborationId) throws NoSuchElementException;

    /**
     * Adds a {@link ShortCollaboration} to the manager.
     * @param collaboration the {@link ShortCollaboration} to be added.
     * @return true if the {@link ShortCollaboration} was not in the manager, false otherwise.
     */
    boolean addCollaboration(ShortCollaboration collaboration);

    /**
     * Removes a {@link ShortCollaboration} from the manager.
     * @param collaborationId the identifier of the {@link ShortCollaboration}.
     * @return true if the {@link ShortCollaboration} was in the manager, false otherwise.
     */
    boolean removeCollaboration(String collaborationId);

    /**
     * Returns a list of {@link ShortCollaboration} filtered by the given {@link CollaborationType}.
     * @param collaborationType the {@link CollaborationType} to filter with.
     * @return the filtered list.
     */
    List<ShortCollaboration> filterByType(CollaborationType collaborationType);

    /**
     * Get a list of all {@link Collaboration}s id.
     * @return a list of all {@link Collaboration}s id.
     */
    List<String> getCollaborationsId();

}
