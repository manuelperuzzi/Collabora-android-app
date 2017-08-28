package org.gammf.collabora_android.collaborations.complete_collaborations.general;

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
    private final Set<Note> notes;

    protected AbstractCollaboration(final String id, final String name) {
        this.id = id;
        this.name = name;
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

        return id != null ? id.equals(that.id) : that.id == null &&
                (name != null ? name.equals(that.name) : that.name == null &&
                        notes.equals(that.notes));

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + notes.hashCode();
        return result;
    }
}
