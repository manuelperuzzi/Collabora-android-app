package org.gammf.collabora_android.notes;

/**
 * @author Alfredo Maffi
 * Concrete state representing a note's state.
 */

public class NoteState implements State {

    private String state;
    private String responsibleUsername;

    /**
     * Class constructor, only the textual state is set.
     * @param state the textual state to be set.
     */
    public NoteState(final String state) {
        this.state = state;
    }

    /**
     * Class constructor, the state is fully set.
     * @param state the textual state to be set.
     * @param responsibleUsername the responsible's username to be set.
     */
    public NoteState(final String state, final String responsibleUsername) {
        this(state);
        this.responsibleUsername = responsibleUsername;
    }

    @Override
    public String getCurrentDefinition() {
        return this.state;
    }

    @Override
    public String getCurrentResponsible() {
        return this.responsibleUsername;
    }

    @Override
    public void setDefinition(String newState) {
        this.state = newState;
    }

    @Override
    public void setResponsible(String newResponsibleUsername) {
        this.responsibleUsername = newResponsibleUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteState noteState = (NoteState) o;

        return state != null ? state.equals(noteState.state) : noteState.state == null &&
                (responsibleUsername != null ? responsibleUsername.equals(noteState.responsibleUsername) : noteState.responsibleUsername == null);

    }

    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (responsibleUsername != null ? responsibleUsername.hashCode() : 0);
        return result;
    }
}
