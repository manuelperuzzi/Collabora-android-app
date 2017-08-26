package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.collaborations.CollaborationType;
import org.gammf.collabora_android.collaborations.short_collaborations.ConcreteShortCollaboration;
import org.gammf.collabora_android.collaborations.short_collaborations.ShortCollaboration;
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
     * @throws JSONException if the conversion went wrong.
     */
    public static JSONObject shortCollaborationToJson(final ShortCollaboration collaboration) throws JSONException {
        final JSONObject json = new JSONObject();

        json.put("id", collaboration.getId());
        json.put("name", collaboration.getName());
        json.put("collaborationType", collaboration.getCollaborationType().name());

        return json;
    }

    /**
     * Create a short collaboration class from a json message.
     * @param json the input json message.
     * @return a short collaboration built from the json message.
     * @throws JSONException if the conversion went wrong.
     */
    public static ShortCollaboration jsonToShortCollaboration(final JSONObject json) throws JSONException {
        final String id = json.getString("id");
        final String name = json.getString("name");
        final CollaborationType collaborationType = CollaborationType.valueOf(json.getString("collaborationType"));

        return new ConcreteShortCollaboration(id, name, collaborationType);
    }

}
