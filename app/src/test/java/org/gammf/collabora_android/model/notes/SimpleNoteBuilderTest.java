package org.gammf.collabora_android.model.notes;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Alfredo Maffi
 * Simple test used to test a note builder.
 */
public class SimpleNoteBuilderTest {
    private Note n;
    @Before
    public void init() {
        n = new SimpleNoteBuilder("content", new NoteState("todo"))
                .setNoteID("noteId")
                .setLocation(new NoteLocation(42.22, 55.23))
                .setExpirationDate(new DateTime())
                .setPreviousNotes(Arrays.asList("test","test2","test3"))
                .buildNote();
    }

    @Test
    public void noteIsBuiltCorrectly() {
        assertEquals(n.getContent(),"content");
        assertEquals(n.getState().getCurrentDefinition(), "todo");
        assertNull(n.getState().getCurrentResponsible());
        assertEquals(n.getPreviousNotes(), Arrays.asList("test","test2","test3"));
        assertEquals(n.getLocation().getLatitude(), 42.22, 0.000001);
        assertEquals(n.getLocation().getLongitude(), 55.23, 0.000001);
    }

    @Test
    public void modifyNoteData() {
        n.setContent("new content");
        n.setState(new NoteState("doing", "fone"));
        n.setExpirationDate(new DateTime(2547362525325L));
        n.setPreviousNotes(Arrays.asList("test3","test2","test1"));
        n.setLocation(new NoteLocation(32.32,12.23));

        assertEquals(n.getContent(),"new content");
        assertEquals(n.getState().getCurrentDefinition(), "doing");
        assertEquals(n.getState().getCurrentResponsible(), "fone");
        assertEquals(n.getPreviousNotes(), Arrays.asList("test3","test2","test1"));
        assertEquals(n.getLocation().getLatitude(), 32.32, 0.000001);
        assertEquals(n.getLocation().getLongitude(), 12.23, 0.000001);
    }
}