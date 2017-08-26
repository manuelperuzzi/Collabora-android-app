package org.gammf.collabora_android.notes;

/**
 * @author Alfredo Maffi
 * Interface representing the state of a note.
 */

public interface State {

    /**
     * @return the note's textual state.
     */
    String getCurrentState();

    /**
     * @return the current note's responsible for the actual state.
     */
    String getCurrentResponsible();

    /**
     * Changes the note's state.
     * @param newState the new textual state to be set.
     */
    void setState(String newState);

    /**
     * Changes the note's responsible.
     * @param newResponsibleUsername the new responsible to be set.
     */
    void setResponsible(String newResponsibleUsername);
}
