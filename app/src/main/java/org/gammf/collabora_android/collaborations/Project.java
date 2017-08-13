package org.gammf.collabora_android.collaborations;

import java.util.NoSuchElementException;

/**
 * @author Manuel Peruzzi
 * Represents a collaboration of type project.
 */
public interface Project extends Collaboration {

    /**
     * @return a list containing each module of the collaboration.
     */
    List<Module> getAllModules();

    /**
     * Returns a module identified by its id.
     * @param moduleId the identifier of the requested module.
     * @return the requested module.
     * @throws NoSuchElementException if the module id does not exist.
     */
    Module getModule(String moduleId) throws NoSuchElementException;

    /**
     * Adds a module to the collaboration. If the module already exist, it will be overwritten.
     * @param module the module to be added to the collaboration.
     * @return true if the module is not in the collaboration, false otherwise.
     */
    boolean addModule(Module module);

    /**
     * Removes a module from the collaboration.
     * @param moduleId the identifier of the module to be removed from the collaboration.
     * @return true if the module was in the collaboration, false otherwise.
     */
    boolean removeModule(String moduleId);

}
