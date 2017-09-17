package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.model.notes.NoteLocation;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.model.notes.NoteState;
import org.gammf.collabora_android.model.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.utils.model.NoteUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Alfredo Maffi
 * Simple test of the conversions in NoteUtils class.
 */
public class NoteUtilsTest {

    private static final List testPreviousNote = Arrays.asList("test", "test2");
    private static final String NOTE_CONTENT = "some content";
    private static final String DOING_STATE = "doing";
    private static final String RESPONSIBLE = "Mattia";
    private static final Double LATITUDE = 45.24;
    private static final Double LONGITUDE = 44.21;
    private static final Long EXPIRATION_DATE = 772408800000L;
    private static final String TITLE = "someTitle";
    @Test
    public void testNoteToJSON() {
        List<String> list = testPreviousNote;
        Note note = new SimpleNoteBuilder(NOTE_CONTENT, new NoteState(DOING_STATE, RESPONSIBLE))
                .setLocation(new NoteLocation(LATITUDE,LONGITUDE))
                .setExpirationDate(new DateTime(EXPIRATION_DATE))
                .setPreviousNotes(testPreviousNote)
                .buildNote();
        try {
            JSONObject obj = NoteUtils.noteToJSON(note);
            assertEquals(obj.getString("content"), NOTE_CONTENT);
            assertEquals((Long)new DateTime(obj.get("expiration")).getMillis(), EXPIRATION_DATE);
            JSONObject state = (JSONObject) obj.get("state");
            assertEquals(state.getString("definition"), DOING_STATE);
            assertEquals(state.getString("responsible"), RESPONSIBLE);
            JSONArray array = (JSONArray)obj.get("previousNotes");
            for(int i = 0; i < array.length(); i++) {
                assertEquals(list.get(i), array.getString(i));
            }
        } catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void testJSONtoNote() {
        JSONObject obj = new JSONObject();
        JSONObject state = new JSONObject();
        try {
            state.put("definition", DOING_STATE).put("responsible", RESPONSIBLE);
            obj.put("content", NOTE_CONTENT).put("title", TITLE).put("state", state);
            Note note = NoteUtils.jsonToNote(obj);
            assertEquals(note.getContent(), NOTE_CONTENT);
            assertEquals(note.getState().getCurrentDefinition(), DOING_STATE);
            assertEquals(note.getState().getCurrentResponsible(), RESPONSIBLE);
            assertNull(note.getPreviousNotes());
        } catch (JSONException e) {
            fail();
        }
    }
}