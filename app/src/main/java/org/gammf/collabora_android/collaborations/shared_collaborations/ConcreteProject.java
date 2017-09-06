package org.gammf.collabora_android.collaborations.shared_collaborations;

import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.Note;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Manuel Peruzzi
 * An implementation of a collaboration of type project.
 */
public class ConcreteProject extends AbstractSharedCollaboration implements Project {

    private final Set<Module> modules;

    /**
     * Class constructor.
     * @param id the identifier of the project.
     * @param name the name of the project.
     */
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
            } catch (final NoSuchElementException e) {
                // keep searching
            }
        }
        return super.getNote(noteId);
    }

    @Override
    public boolean addNote(final Note note, final String moduleId) {
        for (final Module m: modules) {
            if (m.getId().equals(moduleId)) {
                return m.addNote(note);
            }
        }
        return false;
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
}
