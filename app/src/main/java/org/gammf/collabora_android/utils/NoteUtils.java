package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.app.utils.ExceptionManager;
import org.gammf.collabora_android.notes.ModuleNote;
import org.gammf.collabora_android.notes.NoteLocation;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleModuleNote;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.notes.State;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class providing methods to convert from a {@link Note} object to a json object and vice versa.
 */

public class NoteUtils {

    /**
     * Provides a json with all the {@link Note} information.
     * @param note the {@link Note} to be converted.
     * @return a json object with all the {@link Note} information.
     */
    public static JSONObject noteToJSON(final Note note) {
        final JSONObject jsn = new JSONObject();
        try {
            if (note.getNoteID() != null) {
                jsn.put("id", note.getNoteID());
            }
            if (note.getContent() != null) {
                jsn.put("content", note.getContent());
            }
            if (note.getLocation() != null) {
                final JSONObject location = new JSONObject();
                location.put("latitude", note.getLocation().getLatitude());
                location.put("longitude", note.getLocation().getLongitude());
                jsn.put("location", location);
            }
            if (note.getExpirationDate() != null) {
                jsn.put("expiration", note.getExpirationDate());
            }
            if (note.getState() != null) {
                final JSONObject state = new JSONObject();
                state.put("definition", note.getState().getCurrentDefinition());
                if (note.getState().getCurrentResponsible() != null) {
                    state.put("responsible", note.getState().getCurrentResponsible());
                }
                jsn.put("state", state);
            }
            if (note.getPreviousNotes() != null) {
                jsn.put("previousNotes", new JSONArray(note.getPreviousNotes()));
            }
            if (note instanceof ModuleNote) {
                jsn.put("module", ((ModuleNote) note).getModuleId());
            }
        } catch (final JSONException e) {
            ExceptionManager.getInstance().handle(e);
        }
        return jsn;
    }

    /**
     * Creates a {@link Note} class from a json object.
     * @param jsn the input json object.
     * @return a {@link Note} built from the json object.
     */
    public static Note jsonToNote(final JSONObject jsn) {
        Note note = null;
        try {
            final JSONObject state = (JSONObject) jsn.get("state");
            final State noteState = state.has("responsible") ? new NoteState(state.getString("definition"), state.getString("responsible"))
                    : new NoteState(state.getString("definition"));
            final SimpleNoteBuilder builder = new SimpleNoteBuilder(jsn.getString("content"), noteState);
            if (jsn.has("id")) {
                builder.setNoteID(jsn.getString("id"));
            }
            if (jsn.has("location")) {
                final JSONObject location = (JSONObject) jsn.get("location");
                builder.setLocation(new NoteLocation(location.getDouble("latitude"), location.getDouble("longitude")));
            }
            if (jsn.has("expiration")) {
                builder.setExpirationDate(new DateTime(jsn.get("expiration")));
            }
            if (jsn.has("previousNotes")) {
                final List<String> previousNotes = new ArrayList<>();
                final JSONArray pNotes = (JSONArray) jsn.get("previousNotes");
                for (int i = 0; i < pNotes.length(); i++) {
                    previousNotes.add(pNotes.getString(i));
                }
                builder.setPreviousNotes(previousNotes);
            }
            note = builder.buildNote();

            if (jsn.has("module")) {
                return new SimpleModuleNote(note, jsn.getString("module"));
            }
        } catch (final JSONException e) {
            ExceptionManager.getInstance().handle(e);
        }
        return note;
    }
}
