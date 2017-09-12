package org.gammf.collabora_android.app.utils;

import org.gammf.collabora_android.notes.Note;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;


public class NoteComparator implements Comparator<Note> {
    public int compare(final Note c1, final Note c2) {
        final int comp = getEnum(c1.getState().getCurrentState()).compareTo(getEnum(c2.getState().getCurrentState()));
        return (comp == 0) ? Collator.getInstance(Locale.US).compare(c1.getContent(), c2.getContent()) : comp;
    }

    private NoteProjectState getEnum(String state) {
        return NoteProjectState.of(state);
    }
}
