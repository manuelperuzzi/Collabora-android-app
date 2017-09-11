package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.app.utils.ExceptionManager;
import org.gammf.collabora_android.short_collaborations.ConcreteShortCollaboration;
import org.gammf.collabora_android.short_collaborations.ShortCollaboration;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Manuel Peruzzi
 * Utily class providing methods to convert from short collaboration class to json message and vice versa.
 */
public class ShortCollaborationUtils {

    /**
     * Provides a json with all the short collaboration information.
     * @param collaboration the short collaboration.
     * @return a json message with all the short collaboration information.
     */
    public static JSONObject shortCollaborationToJson(final ShortCollaboration collaboration) {
        final JSONObject json = new JSONObject();
        try {
            json.put("id", collaboration.getId());
            json.put("name", collaboration.getName());
            json.put("collaborationType", collaboration.getCollaborationType().name());
        } catch (final JSONException e) {
            ExceptionManager.getInstance().handle(e);
        }
        return json;
    }

    /**
     * Create a short collaboration class from a json message.
     * @param json the input json message.
     * @return a short collaboration built from the json message.
     */
    public static ShortCollaboration jsonToShortCollaboration(final JSONObject json){
        ShortCollaboration collaboration = null;
        try {
            final String id = json.getString("id");
            final String name = json.getString("name");
            final CollaborationType collaborationType = CollaborationType.valueOf(json.getString("collaborationType"));
            collaboration = new ConcreteShortCollaboration(id, name, collaborationType);
        } catch (final JSONException e) {
            ExceptionManager.getInstance().handle(e);
        }
        return collaboration;
    }
}
