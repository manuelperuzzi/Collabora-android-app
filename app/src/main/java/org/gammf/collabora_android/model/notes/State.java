package org.gammf.collabora_android.model.notes;

/**
 * Interface representing the state of a {@link Note}, which is composed by a textual definition and a responsible.
 */

public interface State {

    /**
     * @return the {@link Note}'s textual state.
     */
    String getCurrentDefinition();

    /**
     * @return the current {@link Note}'s responsible for the actual state.
     */
    String getCurrentResponsible();

    /**
     * Changes the {@link Note}'s textual state.
     * @param newState the new textual state to be set.
     */
    void setDefinition(String newState);

    /**
     * Changes the {@link Note}'s responsible.
     * @param newResponsibleUsername the new responsible to be set.
     */
    void setResponsible(String newResponsibleUsername);
}
