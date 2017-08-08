package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.notes.Note;
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
            jsn.put("userID", note.getUsername());
        }
        if(note.getTitle() != null) {
            jsn.put("title", note.getTitle());
        }
        if(note.getContent() != null) {
            jsn.put("content", note.getContent());
        }
        if(note.getLatitude() != null && note.getLongitude() != null) {
            final JSONObject location = new JSONObject();
            location.put("latitude", note.getLatitude());
            location.put("longitude", note.getLongitude());
            jsn.put("location", location);
        }
        if(note.getExpirationDate() != null) {
            jsn.put("expiration", note.getExpirationDate());
        }
        if(note.getState() != null) {
            final JSONObject state = new JSONObject();
            state.put("definition", note.getState());
            state.put("username", note.getStateResponsible());
            jsn.put("state", state);
        }
        if(note.getPreviousNotes() != null) {
            jsn.put("previousNotes", note.getPreviousNotes());
        }
        return jsn;
    }

    public static Note jsonToNote(final JSONObject jsn) throws JSONException{
        final SimpleNoteBuilder builder = new SimpleNoteBuilder(jsn.getString("username"));
        if(jsn.has("id")) {
            builder.setNoteID(jsn.getString("id"));
        }
        if(jsn.has("title")) {
            builder.setTitle(jsn.getString("title"));
        }
        if(jsn.has("content")) {
            builder.setContent(jsn.getString("content"));
        }
        if(jsn.has("location")) {
            final JSONObject location = (JSONObject)jsn.get("location");
            builder.setLatitude(location.getDouble("latitude"));
            builder.setLongitude(location.getDouble("longitude"));
        }
        if(jsn.has("expiration")) {
            builder.setExpirationDate((Date)jsn.get("expiration"));
        }
        if(jsn.has("state")) {
            final JSONObject state = (JSONObject)jsn.get("state");
            builder.setState(state.getString("definition"));
            builder.setStateResponsible(state.getString("username"));
        }
        if(jsn.has("previousNotes")) {
            builder.setPreviousNotes((List<String>)jsn.get("previousNotes"));
        }
        return builder.buildNote();
    }
}
