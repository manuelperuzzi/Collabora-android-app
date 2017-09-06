package org.gammf.collabora_android.notes;

/**
 * @author Manuel Peruzzi
 * Represents a note belonging to a module.
 */
public interface ModuleNote extends Note {

    /**
     * @return the single note.
     */
    Note getNote();

    /**
     * @return the identifier of the module that contains the note.
     */
    String getModuleId();

}
