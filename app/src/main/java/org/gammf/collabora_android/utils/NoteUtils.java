package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.notes.NoteLocation;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by Alfredo on 05/08/2017.
 */

public class NoteUtils {
    public static JSONObject noteToJSON(final Note note) throws JSONException {
        final JSONObject jsn = new JSONObject();
        if(note.getNoteID() != null) {
            jsn.put("id", note.getNoteID());
        }
        if(note.getUsername() != null) {
            jsn.put("user", note.getUsername());
        }
        if(note.getContent() != null) {
            jsn.put("content", note.getContent());
        }
        if(note.getLocation() != null) {
            final JSONObject location = new JSONObject();
            location.put("latitude", note.getLocation().getLatitude());
            location.put("longitude", note.getLocation().getLongitude());
            jsn.put("location", location);
        }
        if(note.getExpirationDate() != null) {
            jsn.put("expiration", note.getExpirationDate());
        }
        if(note.getState() != null) {
            final JSONObject state = new JSONObject();
            state.put("definition", note.getState().getCurrentState());
            state.put("user", note.getState().getCurrentResponsible());
            jsn.put("state", state);
        }
        if(note.getPreviousNotes() != null) {
            jsn.put("previousNotes", note.getPreviousNotes());
        }
        return jsn;
    }

    public static Note jsonToNote(final JSONObject jsn) throws JSONException{
        final SimpleNoteBuilder builder = new SimpleNoteBuilder(jsn.getString("user"));
        if(jsn.has("id")) {
            builder.setNoteID(jsn.getString("id"));
        }
        if(jsn.has("content")) {
            builder.setContent(jsn.getString("content"));
        }
        if(jsn.has("location")) {
            final JSONObject location = (JSONObject)jsn.get("location");
            builder.setLocation(new NoteLocation(location.getDouble("latitude"), location.getDouble("longitude")));
        }
        if(jsn.has("expiration")) {
            builder.setExpirationDate((Date)jsn.get("expiration"));
        }
        if(jsn.has("state")) {
            final JSONObject state = (JSONObject)jsn.get("state");
            builder.setState(new NoteState(state.getString("definition"),state.getString("user")));
        }
        if(jsn.has("previousNotes")) {
            builder.setPreviousNotes((List<String>)jsn.get("previousNotes"));
        }
        return builder.buildNote();
    }
}
