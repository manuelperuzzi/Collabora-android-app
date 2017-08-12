package org.gammf.collabora_android.collaborations;

import org.gammf.collabora_android.notes.Note;

import java.util.List;
import java.util.NoSuchElementException;

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
     * @return a list containing each member of the collaboration.
     */
    List<CollaborationMember> getAllMembers();

    /**
     * Returns a member of the collaboration identified by its user id.
     * @param userId the identifier of the user.
     * @return the requested member.
     * @throws NoSuchElementException if the userId does not exist.
     */
    CollaborationMember getMember(String userId) throws NoSuchElementException;

    /**
     * Adds a user to the collaboration with the specified access right. If the user already exists, it will be overwritten.
     * @param member the user to be added to the collaboration.
     * @return true if the user is not in the collaboration, false otherwise.
     */
    boolean addMember(CollaborationMember member);

    /**
     * @return a list containing each note of the collaboration.
     */
    List<Note> getAllNotes();

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

}
