package org.gammf.collabora_android.model.notes;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This is a test for Simple Module Note.
 */

public class SimpleModuleNoteTest {

    private static final String NOTE_CONTENT = "content note";
    private static final String NOTE_ID = "noteId";
    private static final String EXPIRATION_DATE = "2017-09-16T16:44:15.035+02:00";
    private static final String NEW_EXPIRATION_DATE = "2017-09-18T16:44:15.035+02:00";
    private static final String DOING_STATE = "doing";
    private static final String START_RESPONSIBLE = "John Stone";
    private static final String NEW_RESPONSIBLE = "Marcus Stone";
    private static final String NEW_NOTE_CONTENT = "new content note";
    private static final String DONE_STATE = "done";
    private static final String MODULE_ID = "moduleId";
    private static final List PREVIOUS_NOTES = Arrays.asList("note1", "note2");
    private static final List NEW_PREVIOUS_NOTES = Arrays.asList("note2", "note1");
    private static final Double LATITUDE = 42.12;
    private static final Double LONGITUDE = 12.20;
    private static final Double NEW_LATITUDE = 45.50;
    private static final Double NEW_LONGITUDE = 15.20;
    private static final Double NO_COORDINATE = 0.000001;

    private Note note;
    private ModuleNote moduleNote;

    @Before
    public void init() {
        note = new SimpleNoteBuilder(NOTE_CONTENT, new NoteState(DOING_STATE, START_RESPONSIBLE))
                .setNoteID(NOTE_ID)
                .setLocation(new NoteLocation(LATITUDE, LONGITUDE))
                .setExpirationDate(new DateTime(EXPIRATION_DATE))
                .setPreviousNotes(PREVIOUS_NOTES)
                .buildNote();
        moduleNote = new SimpleModuleNote(note, MODULE_ID);
    }

    @Test
    public void getComponentsOfSimpleModuleNote() {
        assertEquals(note, moduleNote.getNote());
        assertEquals(MODULE_ID, moduleNote.getModuleId());
    }

    @Test
    public void verifyEquality() {
        assertTrue(moduleNote.equals(new SimpleModuleNote(note, MODULE_ID)));
    }

    @Test
    public void checkModuleNoteInfo() {
        assertEquals(moduleNote.getNoteID(), NOTE_ID);
        assertEquals(moduleNote.getContent(), NOTE_CONTENT);
        assertEquals(moduleNote.getLocation().getLatitude(), LATITUDE, NO_COORDINATE);
        assertEquals(moduleNote.getLocation().getLongitude(), LONGITUDE, NO_COORDINATE);
        assertEquals(moduleNote.getExpirationDate(), new DateTime(EXPIRATION_DATE));
        assertEquals(moduleNote.getPreviousNotes(), PREVIOUS_NOTES);
        assertEquals(moduleNote.getState().getCurrentDefinition(), DOING_STATE);
        assertEquals(moduleNote.getState().getCurrentResponsible(), START_RESPONSIBLE);
    }

    @Test
    public void modifyNoteData() {
        moduleNote.setContent(NEW_NOTE_CONTENT);
        moduleNote.setState(new NoteState(DONE_STATE, NEW_RESPONSIBLE));
        moduleNote.setExpirationDate(new DateTime(NEW_EXPIRATION_DATE));
        moduleNote.setPreviousNotes(NEW_PREVIOUS_NOTES);
        moduleNote.setLocation(new NoteLocation(NEW_LATITUDE,NEW_LONGITUDE));

        assertEquals(moduleNote.getContent(),NEW_NOTE_CONTENT);
        assertEquals(moduleNote.getState().getCurrentDefinition(), DONE_STATE);
        assertEquals(moduleNote.getState().getCurrentResponsible(), NEW_RESPONSIBLE);
        assertEquals(moduleNote.getExpirationDate(), new DateTime(NEW_EXPIRATION_DATE));
        assertEquals(moduleNote.getPreviousNotes(), NEW_PREVIOUS_NOTES);
        assertEquals(moduleNote.getLocation().getLatitude(), NEW_LATITUDE, NO_COORDINATE);
        assertEquals(moduleNote.getLocation().getLongitude(), NEW_LONGITUDE, NO_COORDINATE);
    }

}
