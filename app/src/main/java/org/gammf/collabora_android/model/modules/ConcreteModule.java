package org.gammf.collabora_android.model.modules;

import org.gammf.collabora_android.model.notes.ModuleNote;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.model.notes.SimpleModuleNote;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * An implementation of a module in a collaboration.
 */
public class ConcreteModule implements Module {

    private final String id;
    private String description;
    private final Set<ModuleNote> notes;
    private String state;

    /**
     * Class constructor.
     * @param id the identifier of the module.
     * @param description the description of the module.
     * @param state the state definition of the module.
     */
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
    public boolean containsNote(final String noteId) {
        for (final ModuleNote mn: notes) {
            if (mn.getNoteID().equals(noteId)) {
                return true;
            }
        }
        return false;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConcreteModule that = (ConcreteModule) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
