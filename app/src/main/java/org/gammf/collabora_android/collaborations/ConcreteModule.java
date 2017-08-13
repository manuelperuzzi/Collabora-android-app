package org.gammf.collabora_android.collaborations;

import org.gammf.collabora_android.notes.ModuleNote;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.SimpleModuleNote;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Manuel Peruzzi
 * An implementation of a module in a collaboration.
 */
public class ConcreteModule implements Module {

    private final String id;
    private String description;
    private final Set<ModuleNote> notes;
    private String state;

    public ConcreteModule(final String id, final String description, final String state) {
        this.id = id;
        this.description = description;
        this.state = state;
        this.notes = new HashSet<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public Set<ModuleNote> getAllNotes() {
        return Collections.unmodifiableSet(notes);
    }

    @Override
    public ModuleNote getNote(final String noteId) throws NoSuchElementException {
        for (final ModuleNote mn: notes) {
            if (mn.getNoteID().equals(noteId)) {
                return mn;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean addNote(final Note note) {
        return notes.add(new SimpleModuleNote(note, id));
    }

    @Override
    public boolean removeNote(final String noteId) {
        for (final ModuleNote mn: notes) {
            if (mn.getNoteID().equals(noteId)) {
                notes.remove(mn);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getStateDefinition() {
        return state;
    }

    @Override
    public void setStateDefinition(final String stateDefinition) {
        this.state = stateDefinition;
    }
}
