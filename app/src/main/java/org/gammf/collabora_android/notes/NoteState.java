package org.gammf.collabora_android.notes;

/**
 * @author Alfredo Maffi
 * Concrete state representing a note's state.
 */

public class NoteState implements State {

    private String state;
    private String responsibleUsername;

    /**
     * First class constructor, only the textual state is set.
     * @param state the textual state to be set.
     */
    public NoteState(final String state) {
        this.state = state;
    }

    /**
     * Second class constructor, the NoteState is fully set.
     * @param state the textual state to be set.
     * @param responsibleUsername the responsible's username to be set.
     */
    public NoteState(final String state, final String responsibleUsername) {
        this(state);
        this.responsibleUsername = responsibleUsername;
    }

    @Override
    public String getCurrentState() {
        return this.state;
    }

    @Override
    public String getCurrentResponsible() {
        return this.responsibleUsername;
    }

    @Override
    public void setState(String newState) {
        this.state = newState;
    }

    @Override
    public void setResponsible(String newResponsibleUsername) {
        this.responsibleUsername = newResponsibleUsername;
    }
}
