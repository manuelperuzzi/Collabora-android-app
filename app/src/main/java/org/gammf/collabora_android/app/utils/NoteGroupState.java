package org.gammf.collabora_android.app.utils;

/**
 * An enum representing note states in personale or group collaborations..
 */
public enum NoteGroupState {
    TO_DO("To Do"),
    DOING("Doing"),
    DONE("Done");

    private final String stateName;

    NoteGroupState(final String name) {
        this.stateName = name;
    }

    @Override
    public String toString() {
        return this.stateName;
    }
};
