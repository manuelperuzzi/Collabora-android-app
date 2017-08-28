package org.gammf.collabora_android.collaborations.complete_collaborations;

import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.users.CollaborationMember;

import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Manuel Peruzzi
 * Simple interface that represents a collaboration shared between users.
 */
public interface SharedCollaboration extends Collaboration {

    /**
     * @return a list containing each member of the collaboration.
     */
    Set<CollaborationMember> getAllMembers();

    /**
     * Checks if the given user is a member of the collaboration.
     * @param username the identifier of the user.
     * @return true if the user is a member of the collaboration, false otherwise.
     */
    boolean containsMember(String username);

    /**
     * Returns a member of the collaboration identified by its user id.
     * @param username the identifier of the user.
     * @return the requested member.
     * @throws NoSuchElementException if the userId does not exist.
     */
    CollaborationMember getMember(String username) throws NoSuchElementException;

    /**
     * Adds a user to the collaboration with the specified access right. If the user already exists, it will be overwritten.
     * @param member the user to be added to the collaboration.
     * @return true if the user is not in the collaboration, false otherwise.
     */
    boolean addMember(CollaborationMember member);

    /**
     * Removes a member from the collaboration.
     * @param username the identifier of the user to be removed from the collaboration.
     * @return true if the user was in the collaboration, false otherwise.
     */
    boolean removeMember(String username);

}
