package org.gammf.collabora_android.model.notes;

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
                .setNoteID("moduleNoteID")
                .setLocation(new NoteLocation(42.12, 12.20))
                .setExpirationDate(new DateTime("2017-09-16T16:44:15.035+02:00"))
                .setPreviousNotes(Arrays.asList("note1", "note2"))
                .buildNote();
        moduleNote = new SimpleModuleNote(note, "moduleId");
    }

    @Test
    public void getComponentsOfSimpleModuleNote() {
        assertEquals(note, moduleNote.getNote());
        assertEquals("moduleId", moduleNote.getModuleId());
    }

    @Test
    public void verifyEquality() {
        assertTrue(moduleNote.equals(new SimpleModuleNote(note, "moduleId")));
    }

    @Test
    public void checkModuleNoteInfo() {
        assertEquals(moduleNote.getNoteID(), "moduleNoteID");
        assertEquals(moduleNote.getContent(), "content note");
        assertEquals(moduleNote.getLocation().getLatitude(), 42.12, 0.000001);
        assertEquals(moduleNote.getLocation().getLongitude(), 12.20, 0.000001);
        assertEquals(moduleNote.getExpirationDate(), new DateTime("2017-09-16T16:44:15.035+02:00"));
        assertEquals(moduleNote.getPreviousNotes(), Arrays.asList("note1", "note2"));
        assertEquals(moduleNote.getState().getCurrentDefinition(), "doing");
        assertEquals(moduleNote.getState().getCurrentResponsible(), "John Stone");
    }

    @Test
    public void modifyNoteData() {
        moduleNote.setContent("new content note");
        moduleNote.setState(new NoteState("done", "Marcus Stone"));
        moduleNote.setExpirationDate(new DateTime("2017-09-18T16:44:15.035+02:00"));
        moduleNote.setPreviousNotes(Arrays.asList("note2", "note1"));
        moduleNote.setLocation(new NoteLocation(32.32,12.23));

        assertEquals(moduleNote.getContent(),"new content note");
        assertEquals(moduleNote.getState().getCurrentDefinition(), "done");
        assertEquals(moduleNote.getState().getCurrentResponsible(), "Marcus Stone");
        assertEquals(moduleNote.getExpirationDate(), new DateTime("2017-09-18T16:44:15.035+02:00"));
        assertEquals(moduleNote.getPreviousNotes(), Arrays.asList("note2", "note1"));
        assertEquals(moduleNote.getLocation().getLatitude(), 32.32, 0.000001);
        assertEquals(moduleNote.getLocation().getLongitude(), 12.23, 0.000001);
    }

}
