package org.gammf.collabora_android.utils;

import android.util.Log;

import org.gammf.collabora_android.notes.ModuleNote;
import org.gammf.collabora_android.notes.NoteLocation;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleModuleNote;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
            if(note.getState().getCurrentResponsible() != null) {
                state.put("responsible", note.getState().getCurrentResponsible());
            }
            jsn.put("state", state);
        }
        if(note.getPreviousNotes() != null) {
            jsn.put("previousNotes", note.getPreviousNotes());
        }
        if(note instanceof ModuleNote) {
            jsn.put("module", ((ModuleNote) note).getModuleId());
        }
        return jsn;
    }

    public static Note jsonToNote(final JSONObject jsn) throws JSONException{
        final SimpleNoteBuilder builder = new SimpleNoteBuilder(jsn.getString("content"));
        if(jsn.has("id")) {
            builder.setNoteID(jsn.getString("id"));
        }
        if(jsn.has("location")) {
            final JSONObject location = (JSONObject)jsn.get("location");
            builder.setLocation(new NoteLocation(location.getDouble("latitude"), location.getDouble("longitude")));
        }
        if(jsn.has("expiration")) {
            builder.setExpirationDate(new DateTime(jsn.get("expiration")));
        }
        if(jsn.has("state")) {
            final JSONObject state = (JSONObject)jsn.get("state");
            builder.setState(state.has("responsible") ? new NoteState(state.getString("definition"),state.getString("responsible"))
                                                      : new NoteState(state.getString("definition")));
        }
        if(jsn.has("previousNotes")) {
            final List<String> previousNotes = new ArrayList<>();
            final JSONArray pNotes = (JSONArray)jsn.get("previousNotes");
            for(int i = 0; i < pNotes.length(); i++) {
                previousNotes.add(pNotes.getString(i));
            }
            builder.setPreviousNotes(previousNotes);
        }
        final Note note = builder.buildNote();

        if (jsn.has("module")) {
            return new SimpleModuleNote(note, jsn.getString("module"));
        }

        return note;
    }
}
