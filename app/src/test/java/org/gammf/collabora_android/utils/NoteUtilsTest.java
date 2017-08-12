package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.notes.NoteLocation;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Alfredo on 05/08/2017.
 */
public class NoteUtilsTest {

    @Test
    public void testNoteToJSON() {
        Note note = new SimpleNoteBuilder("some content")
                .setLocation(new NoteLocation(45.24,44.21))
                .setExpirationDate(new DateTime(772408800000L))
                .setPreviousNotes(new ArrayList<String>(Arrays.asList("test", "test2")))
                .buildNote();
        try {
            JSONObject obj = NoteUtils.noteToJSON(note);
            assertEquals(obj.getString("content"), "some content");
            assertEquals(DateTime.parse(obj.getString("expiration")).getMillis(), 772408800000L);
            assertEquals((List<String>)obj.get("previousNotes"), Arrays.asList("test", "test2"));
            assertNull(obj.getString("id"));
            fail();
        } catch (JSONException e) {
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
            assertEquals(note.getState().getCurrentState(), "doing");
            assertEquals(note.getState().getCurrentResponsible(), "fone");
            assertNull(note.getPreviousNotes());
        } catch (JSONException e) {
            fail();
        }
    }
}