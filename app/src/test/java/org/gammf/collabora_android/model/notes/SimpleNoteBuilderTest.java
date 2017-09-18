package org.gammf.collabora_android.model.notes;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Simple test used to test a note builder.
 */
public class SimpleNoteBuilderTest {

    private static final String NOTE_CONTENT = "content note";
    private static final String NOTE_ID = "noteId";
    private static final String NEW_EXPIRATION_DATE = "2017-09-18T16:44:15.035+02:00";
    private static final String TODO_STATE = "todo";
    private static final String RESPONSIBLE = "John Stone";
    private static final String NEW_NOTE_CONTENT = "new content note";
    private static final String DONE_STATE = "done";
    private static final List PREVIOUS_NOTES = Arrays.asList("test","test2","test3");
    private static final List NEW_PREVIOUS_NOTES = Arrays.asList("test3","test2","test1");
    private static final Double LATITUDE = 42.22;
    private static final Double LONGITUDE = 55.23;
    private static final Double NEW_LATITUDE = 33.21;
    private static final Double NEW_LONGITUDE = 12.23;
    private static final Double NO_COORDINATE = 0.000001;

    private Note n;
    @Before
    public void init() {
        n = new SimpleNoteBuilder(NOTE_CONTENT, new NoteState(TODO_STATE))
                .setNoteID(NOTE_ID)
                .setLocation(new NoteLocation(LATITUDE, LONGITUDE))
                .setExpirationDate(new DateTime())
                .setPreviousNotes(PREVIOUS_NOTES)
                .buildNote();
    }

    @Test
    public void noteIsBuiltCorrectly() {
        assertEquals(n.getNoteID(), NOTE_ID);
        assertEquals(n.getContent(), NOTE_CONTENT);
        assertEquals(n.getState().getCurrentDefinition(), TODO_STATE);
        assertNull(n.getState().getCurrentResponsible());
        assertEquals(n.getPreviousNotes(), PREVIOUS_NOTES);
        assertEquals(n.getLocation().getLatitude(), LATITUDE, NO_COORDINATE);
        assertEquals(n.getLocation().getLongitude(), LONGITUDE, NO_COORDINATE);
    }

    @Test
    public void modifyNoteData() {
        n.setContent(NEW_NOTE_CONTENT);
        n.setState(new NoteState(DONE_STATE, RESPONSIBLE));
        n.setExpirationDate(new DateTime(NEW_EXPIRATION_DATE));
        n.setPreviousNotes(NEW_PREVIOUS_NOTES);
        n.setLocation(new NoteLocation(NEW_LATITUDE,NEW_LONGITUDE));

        assertEquals(n.getContent(),NEW_NOTE_CONTENT);
        assertEquals(n.getState().getCurrentDefinition(), DONE_STATE);
        assertEquals(n.getState().getCurrentResponsible(), RESPONSIBLE);
        assertEquals(n.getPreviousNotes(),NEW_PREVIOUS_NOTES);
        assertEquals(n.getLocation().getLatitude(), NEW_LATITUDE, NO_COORDINATE);
        assertEquals(n.getLocation().getLongitude(), NEW_LONGITUDE, NO_COORDINATE);
    }
}