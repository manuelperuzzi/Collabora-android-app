package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alfredo on 05/08/2017.
 */

public class NoteUtils {
    public static JSONObject noteToJSON(final Note note) throws JSONException {
        final JSONObject jsn = new JSONObject();
        jsn.put("userID", note.getUserID());
        if(note.getTitle() != null) {
            jsn.put("title", note.getTitle());
        }
        if(note.getContent() != null) {
            jsn.put("content", note.getContent());
        }
        return jsn;
    }

    public static Note jsonToNote(final JSONObject jsn) throws JSONException{
        final SimpleNoteBuilder builder = new SimpleNoteBuilder(jsn.getString("userID"));
        if(jsn.has("title")) {
            builder.setTitle(jsn.getString("title"));
        }
        if(jsn.has("content")) {
            builder.setContent(jsn.getString("content"));
        }
        return builder.buildNote();
    }
}
