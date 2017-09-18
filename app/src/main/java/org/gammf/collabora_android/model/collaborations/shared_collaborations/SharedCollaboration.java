package org.gammf.collabora_android.model.collaborations.shared_collaborations;

import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.model.users.CollaborationMember;

import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Simple interface that represents a {@link Collaboration} shared between users.
 */
public interface SharedCollaboration extends Collaboration {

    /**
     * @return a list containing each {@link CollaborationMember} of the {@link Collaboration}.
     */
    Set<CollaborationMember> getAllMembers();

    /**
     * Checks if the user corresponding to the given username is a member of the {@link Collaboration}.
     * @param username the identifier of the user.
     * @return true if the user is a member of the collaboration, false otherwise.
     */
    boolean containsMember(String username);

    /**
     * Returns a {@link CollaborationMember} of the {@link Collaboration} identified by its username.
     * @param username the identifier of the user.
     * @return the requested {@link CollaborationMember}.
     * @throws NoSuchElementException if the {@link CollaborationMember} does not exist.
     */
    CollaborationMember getMember(String username) throws NoSuchElementException;

    /**
     * Adds a {@link CollaborationMember} to the {@link Collaboration} with the specified access right.
     * If the member already belongs to the it, it will be overwritten.
     * @param member the {@link CollaborationMember} to be added to the {@link Collaboration}.
     * @return true if the {@link CollaborationMember} is not in the {@link Collaboration}, false otherwise.
     */
    boolean addMember(CollaborationMember member);

    /**
     * Removes a {@link CollaborationMember} from the collaboration.
     * @param username the username of the {@link CollaborationMember} to be removed from the collaboration.
     * @return true if the {@link CollaborationMember} was in the {@link Collaboration}, false otherwise.
     */
    boolean removeMember(String username);

}
