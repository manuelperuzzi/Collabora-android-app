package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.notes.NoteLocation;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
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

    @Test
    public void testNoteToJSON() {
        List<String> list = Arrays.asList("test", "test2");
        Note note = new SimpleNoteBuilder("some content", new NoteState("doing", "fone"))
                .setLocation(new NoteLocation(45.24,44.21))
                .setExpirationDate(new DateTime(772408800000L))
                .setPreviousNotes(Arrays.asList("test", "test2"))
                .buildNote();
        try {
            JSONObject obj = NoteUtils.noteToJSON(note);
            assertEquals(obj.getString("content"), "some content");
            assertEquals(new DateTime(obj.get("expiration")).getMillis(), 772408800000L);
            JSONObject state = (JSONObject) obj.get("state");
            assertEquals(state.getString("definition"), "doing");
            assertEquals(state.getString("responsible"), "fone");
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
            state.put("definition", "doing").put("responsible", "fone");
            obj.put("content", "someContent").put("title", "someTitle").put("state", state);
            Note note = NoteUtils.jsonToNote(obj);
            assertEquals(note.getContent(), "someContent");
            assertEquals(note.getState().getCurrentDefinition(), "doing");
            assertEquals(note.getState().getCurrentResponsible(), "fone");
            assertNull(note.getPreviousNotes());
        } catch (JSONException e) {
            fail();
        }
    }
}