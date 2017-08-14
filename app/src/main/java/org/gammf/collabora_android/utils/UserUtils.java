package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.users.AccessRight;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.users.SimpleUser;
import org.gammf.collabora_android.users.User;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Manuel Peruzzi
 * Utily class providing methods to convert from user class to json message and vice versa.
 */
public class UserUtils {

    /**
     * Provides a json with all the user information.
     * @param user the user.
     * @return a json message with all the user information.
     * @throws JSONException if the conversion went wrong.
     */
    public static JSONObject userToJson(final User user) throws JSONException {
        final JSONObject json = new JSONObject();

        json.put("username", user.getUsername());
        json.put("email", user.getEmail());
        json.put("name", user.getName());
        json.put("surname", user.getSurname());
        json.put("birthday", user.getBirthday());
        if (user instanceof CollaborationMember) {
            json.put("right", ((CollaborationMember) user).getAccessRight());
        }

        return json;
    }

    /**
     * Create a user class from a json message.
     * @param json the input json message.
     * @return a user built from the json message.
     * @throws JSONException if the conversion went wrong.
     */
    public static User jsonToUser(final JSONObject json) throws JSONException {
        final SimpleUser.Builder userBuilder = new SimpleUser.Builder();

        userBuilder.username(json.getString("username"));
        userBuilder.email(json.getString("email"));
        userBuilder.name(json.getString("name"));
        userBuilder.surname(json.getString("surname"));
        userBuilder.birthday(new DateTime(json.get("birthday")));
        final User user = userBuilder.build();

        if (json.has("right")) {
            return new CollaborationMember(user, AccessRight.valueOf(json.getString("right")));
        }
        return user;
    }

}
