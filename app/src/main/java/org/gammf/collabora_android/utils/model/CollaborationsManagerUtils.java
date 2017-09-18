package org.gammf.collabora_android.utils.model;

import org.gammf.collabora_android.app.utils.ExceptionManager;
import org.gammf.collabora_android.model.short_collaborations.CollaborationsManager;
import org.gammf.collabora_android.model.short_collaborations.ConcreteCollaborationManager;
import org.gammf.collabora_android.model.short_collaborations.ShortCollaboration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * Utility class providing methods to convert all the {@link ShortCollaboration}s contained in a {@link CollaborationsManager} to
 * a json object and vice versa.
 */
public class CollaborationsManagerUtils {

    /**
     * Provides a json with all the {@link ShortCollaboration}s included in the {@link CollaborationsManager}.
     * @param manager the {@link CollaborationsManager}.
     * @return a json message with all the {@link ShortCollaboration} in the {@link CollaborationsManager}.
     */
    public static JSONObject collaborationsManagerToJson(final CollaborationsManager manager) {
        final JSONObject json = new JSONObject();
        try {
            final Set<ShortCollaboration> collaborations = manager.getAllCollaborations();
            if (!collaborations.isEmpty()) {
                final JSONArray jCollaborations = new JSONArray();
                for (final ShortCollaboration c: collaborations) {
                    jCollaborations.put(ShortCollaborationUtils.shortCollaborationToJson(c));
                }
                json.put("collaborations", jCollaborations);
            }
        } catch (final JSONException e) {
            ExceptionManager.getInstance().handle(e);
        }
        return json;
    }

    /**
     * Creates a {@link CollaborationsManager} instance from a json object.
     * @param json the input json object.
     * @return a {@link CollaborationsManager} built from the json object.
     */
    public static CollaborationsManager jsonToCollaborationManager(final JSONObject json) {
        final CollaborationsManager manager = new ConcreteCollaborationManager();
        try {
            if (json.has("collaborations")) {
                final JSONArray jCollaborations = json.getJSONArray("collaborations");
                for (int i = 0; i < jCollaborations.length(); i++) {
                    final ShortCollaboration collaboration = ShortCollaborationUtils.jsonToShortCollaboration(
                            jCollaborations.getJSONObject(i));
                    manager.addCollaboration(collaboration);
                }
            }
        } catch (final JSONException e) {
            ExceptionManager.getInstance().handle(e);
        }
        return manager;
    }
}
