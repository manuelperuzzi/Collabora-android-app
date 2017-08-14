package org.gammf.collabora_android.collaborations;

import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.ModuleNote;
import org.gammf.collabora_android.notes.Note;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Manuel Peruzzi
 * An implementation of a collaboration of type project.
 */
public class ConcreteProject extends AbstractCollaboration implements Project {

    private final Set<Module> modules;

    public ConcreteProject(final String id, final String name) {
        super(id, name);
        modules = new HashSet<>();
    }

    @Override
    public Set<Module> getAllModules() {
        return Collections.unmodifiableSet(modules);
    }

    @Override
    public boolean containsModule(final String moduleId) {
        for (final Module m: modules) {
            if (m.getId().equals(moduleId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Module getModule(final String moduleId) throws NoSuchElementException {
        for (final Module m: modules) {
            if (m.getId().equals(moduleId)) {
                return m;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean addModule(final Module module) {
        return modules.add(module);
    }

    @Override
    public boolean removeModule(final String moduleId) {
        for (final Module m: modules) {
            if (m.getId().equals(moduleId)) {
                modules.remove(m);
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Note> getAllNotes() {
        final Set<Note> allNotes = new HashSet<>();
        for (final Module m: modules) {
            allNotes.addAll(m.getAllNotes());
        }
        allNotes.addAll(super.getAllNotes());
        return allNotes;
    }

    @Override
    public boolean containsNote(final String noteId) {
        for (final Module m: modules) {
            if (m.containsNote(noteId)) {
                return true;
            }
        }
        return super.containsNote(noteId);
    }

    @Override
    public Note getNote(final String noteId) throws NoSuchElementException {
        for (final Module m: modules) {
            try {
                return m.getNote(noteId);
            } catch (final NoSuchElementException e) { }
        }
        return super.getNote(noteId);
    }

    @Override
    public boolean addNote(final Note note) {
        if (note instanceof ModuleNote) {
            for (final Module m: modules) {
                if (m.getId().equals(((ModuleNote) note).getModuleId())) {
                    return m.addNote(note);
                }
            }
        }
        return super.addNote(note);
    }

    @Override
    public boolean removeNote(final String noteId) {
        for (final Module m: modules) {
            if (m.removeNote(noteId)) {
                return true;
            }
        }
        return super.removeNote(noteId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ConcreteProject that = (ConcreteProject) o;

        return modules.equals(that.modules);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + modules.hashCode();
        return result;
    }

}
