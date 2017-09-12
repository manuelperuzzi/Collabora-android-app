package org.gammf.collabora_android.app.utils;


import org.gammf.collabora_android.modules.Module;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class ModuleComparator implements Comparator<Module> {
    public int compare(final Module c1, final Module c2) {
        final int comp = getEnum(c1.getStateDefinition()).compareTo(getEnum(c2.getStateDefinition()));
        return (comp == 0) ? Collator.getInstance(Locale.US).compare(c1.getDescription(), c2.getDescription()) : comp;
    }

    private NoteProjectState getEnum(String state) {
        return NoteProjectState.of(state);
    }
}
