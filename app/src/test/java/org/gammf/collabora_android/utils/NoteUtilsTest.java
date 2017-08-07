package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Alfredo on 05/08/2017.
 */
public class NoteUtilsTest {

    @Test
    public void testNoteToJSON() {
        Note note = new SimpleNoteBuilder("id")
                .setTitle("myTitle")
                .setLatitude(45.24)
                .setLongitude(44.21)
                .setExpirationDate(new Date(772408800000L))
                .setPreviousNotes(new ArrayList<String>(Arrays.asList("test", "test2")))
                .buildNote();
        try {
            JSONObject obj = NoteUtils.noteToJSON(note);
            assertEquals(obj.getString("userID"), "id");
            assertEquals(obj.getString("title"), "myTitle");
            assertEquals(((Date)obj.get("expiration")).getTime(), 772408800000L);
            assertEquals((List<String>)obj.get("previousNotes"), Arrays.asList("test", "test2"));
            assertNull(obj.getString("content"));
            fail();
        } catch (JSONException e) {}
    }

    @Test
    public void testJSONtoNote() {
        JSONObject obj = new JSONObject();
        JSONObject state = new JSONObject();
        try {
            state.put("definition", "doing").put("username", "fone");
            obj.put("username", "username").put("title", "someTitle").put("state", state);
            Note note = NoteUtils.jsonToNote(obj);
            assertEquals(note.getUsername(), "username");
            assertEquals(note.getTitle(), "someTitle");
            assertEquals(note.getState(), "doing");
            assertEquals(note.getStateResponsible(), "fone");
            assertNull(note.getContent());
            assertNull(note.getPreviousNotes());
        } catch (JSONException e) {
            fail();
        }
    }
}