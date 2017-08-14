package org.gammf.collabora_android.notes;

/**
 * Created by Alfredo on 08/08/2017.
 */

public class NoteState implements State {

    private String state;
    private String responsibleUsername;

    public NoteState(final String state) {
        this.state = state;
    }

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
