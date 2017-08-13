package org.gammf.collabora_android.collaborations;

import org.gammf.collabora_android.notes.Note;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Manuel Peruzzi
 * This is an abstract class that defines the basic operations of a generic collaboration.
 */
public abstract class AbstractCollaboration implements Collaboration {

    private final String id;
    private String name;
    private final Set<CollaborationMember> members;
    private final Set<Note> notes;

    protected AbstractCollaboration(final String id, final String name) {
        this.id = id;
        this.name = name;
        this.members = new HashSet<>();
        this.notes = new HashSet<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public Set<CollaborationMember> getAllMembers() {
        return Collections.unmodifiableSet(members);
    }

    @Override
    public boolean containsMember(String username) {
        for (final CollaborationMember m: members) {
            if (m.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CollaborationMember getMember(String username) throws NoSuchElementException {
        for (final CollaborationMember m: members) {
            if (m.getUsername().equals(username)) {
                return m;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean addMember(CollaborationMember member) {
        return members.add(member);
    }

    @Override
    public boolean removeMember(String username) {
        for (final CollaborationMember m: members) {
            if (m.getUsername().equals(username)) {
                members.remove(m);
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Note> getAllNotes() {
        return Collections.unmodifiableSet(notes);
    }

    @Override
    public boolean containsNote(String noteId) {
        for (final Note n: notes) {
            if (n.getNoteID().equals(noteId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Note getNote(String noteId) throws NoSuchElementException {
        for (final Note n: notes) {
            if (n.getNoteID().equals(noteId)) {
                return n;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean addNote(Note note) {
        return notes.add(note);
    }

    @Override
    public boolean removeNote(String noteId) {
        for (final Note n: notes) {
            if (n.getNoteID().equals(noteId)) {
                notes.remove(n);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractCollaboration that = (AbstractCollaboration) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        if (!members.equals(that.members)) return false;
        return notes.equals(that.notes);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + members.hashCode();
        result = 31 * result + notes.hashCode();
        return result;
    }
}
