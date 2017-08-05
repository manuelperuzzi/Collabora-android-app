package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alfredo on 05/08/2017.
 */
public class NoteUtilsTest {

    @Test
    public void testNoteToJSON() {
        Note note = new SimpleNoteBuilder("id").setTitle("myTitle").buildNote();
        try {
            JSONObject obj = NoteUtils.noteToJSON(note);
            assertEquals(obj.getString("userID"), "id");
            assertEquals(obj.getString("title"), "myTitle");
            assertNull(obj.getString("content"));
            fail();
        } catch (JSONException e) {}
    }

    @Test
    public void testJSONtoNote() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("userID", "id").put("title", "someTitle");
            Note note = NoteUtils.jsonToNote(obj);
            assertEquals(note.getUserID(), "id");
            assertEquals(note.getTitle(), "someTitle");
            assertNull(note.getContent());
        } catch (JSONException e) {
            fail();
        }
    }
}