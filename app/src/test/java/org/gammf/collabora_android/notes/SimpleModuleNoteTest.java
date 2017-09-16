package org.gammf.collabora_android.notes;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Mattia on 16/09/2017.
 */

public class SimpleModuleNoteTest {

    private Note note;
    private ModuleNote moduleNote;

    @Before
    public void init() {
        note = new SimpleNoteBuilder("content note", new NoteState("doing", "John Stone"))
                .setLocation(new NoteLocation(42.12, 12.20))
                .setExpirationDate(new DateTime())
                .setPreviousNotes(Arrays.asList("note1", "note2"))
                .buildNote();

        moduleNote = new SimpleModuleNote(note, "moduleId");
    }

    @Test
    public void getComponents() {
        assertEquals(note, moduleNote.getNote());
        assertEquals("moduleId", moduleNote.getModuleId());
    }

    @Test
    public void verifyEquals() {
        assertTrue(moduleNote.equals(new SimpleModuleNote(note, "moduleId")));
    }

    @Test
    public void modifyNoteData() {

        moduleNote.modifyContent("new content note");
        moduleNote.modifyState(new NoteState("done", "John Stone"));
        moduleNote.modifyExpirationDate(new DateTime(1234567891234L));
        moduleNote.modifyPreviousNotes(Arrays.asList("note2", "note1"));
        moduleNote.modifyLocation(new NoteLocation(32.32,12.23));

        assertEquals(moduleNote.getContent(),"new content note");
        assertEquals(moduleNote.getState().getCurrentState(), "done");
        assertEquals(moduleNote.getState().getCurrentResponsible(), "John Stone");
        assertEquals(moduleNote.getPreviousNotes(), Arrays.asList("note2", "note1"));
        assertEquals(moduleNote.getLocation().getLatitude(), 32.32, 0.000001);
        assertEquals(moduleNote.getLocation().getLongitude(), 12.23, 0.000001);
    }

}
