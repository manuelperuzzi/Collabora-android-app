package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.app.utils.ExceptionManager;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.users.SimpleCollaborationMember;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Alfredo Maffi, Manuel Peruzzi
 * Utily class providing methods to convert from collaboration member class to json message and vice versa.
 */
public class CollaborationMemberUtils {

    /**
     * Provides a json with all the member information.
     * @param member the member.
     * @return a json message with all the member information.
     */
    public static JSONObject memberToJson(final CollaborationMember member) {
        final JSONObject json = new JSONObject();
        try {
            json.put("user", member.getUsername());
            json.put("right", member.getAccessRight().name());
        } catch (final JSONException e) {
            ExceptionManager.getInstance().handle(e);
        }
        return json;
    }

    /**
     * Create a member class from a json message.
     * @param json the input json message.
     * @return a member built from the json message.
     */
    public static CollaborationMember jsonToMember(final JSONObject json) {
        CollaborationMember member = null;
        try {
            final String username = json.getString("user");
            final AccessRight right = AccessRight.valueOf(json.getString("right"));
            member = new SimpleCollaborationMember(username, right);
        } catch(final JSONException e) {
            ExceptionManager.getInstance().handle(e);
        }
        return member;
    }
}
