package org.gammf.collabora_android.short_collaborations;

import org.gammf.collabora_android.utils.CollaborationType;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Manuel Peruzzi
 * Represents a manager that handles all the collaborations of the application, in their short format.
 */
public interface CollaborationsManager {

    /**
     * @return a set containing each collaboration.
     */
    Set<ShortCollaboration> getAllCollaborations();

    /**
     * Checks if the given collaboration is present.
     * @param collaborationId the identifier of the collaboration.
     * @return true if the collaboration is in the manager, false otherwise.
     */
    boolean containsCollaboration(String collaborationId);

    /**
     * Returns a short collaboration identified by its id.
     * @param collaborationId the identifier of the requested collaboration.
     * @return the requested collaboration.
     * @throws NoSuchElementException if the collaboration id does not exist in the manager.
     */
    ShortCollaboration getCollaboration(String collaborationId) throws NoSuchElementException;

    /**
     * Adds a collaboration to the manager.
     * @param collaboration the collaboration to be added.
     * @return true if the collaboration was not in the manager, false otherwise.
     */
    boolean addCollaboration(ShortCollaboration collaboration);

    /**
     * Removes a collaboration from the manager.
     * @param collaborationId the identifier of the collaboration.
     * @return true if the collaboration was in the manager, false otherwise.
     */
    boolean removeCollaboration(String collaborationId);

    /**
     * Returns a list of {@link ShortCollaboration} filtered by the given {@link CollaborationType}
     * @param collaborationType the type to filter with.
     * @return the filtered list.
     */
    List<ShortCollaboration> filterByGroup(CollaborationType collaborationType);

    /**
     * Get a list of all collaborations id.
     * @return a list of all collaborations id.
     */
    List<String> getCollaborationsId();

}
